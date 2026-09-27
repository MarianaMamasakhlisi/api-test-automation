# API Test Automation: Swagger Petstore

REST API test suite for the [Swagger Petstore](https://petstore.swagger.io/#/) demo API, built with Java, Maven,
RestAssured and Cucumber (Gherkin/BDD), run through the JUnit 5 platform. Covers all three resource groups of the
API: `/pet`, `/store` and `/user`.

## Stack

- Java 17, Maven
- Cucumber 7 (`cucumber-java`) + `cucumber-junit-platform-engine`, scenarios run in parallel
- RestAssured 5, with JSON Schema validation for response bodies
- Jackson (for request/response (de)serialization)
- Allure + Cucumber's own HTML report
- GitHub Actions CI on every push and pull request

## Project layout

```
src/test/java/apitests/
  runner/RunCucumberTest.java   JUnit 5 suite entry point (@Suite, glue + plugin config)
  config/ApiConfig.java         Reads config.properties, allows -D overrides
  client/                       RestAssured wrappers: PetClient, OrderClient, UserClient
  models/                       Pet, Category, Tag, Order, User request/response POJOs
  support/TestContext.java      Scenario-scoped state shared between steps and hooks
  hooks/Hooks.java              Sets the base URI; deletes any pet/order/user a scenario created
  stepdefinitions/common/       Assertions shared by positive and negative scenarios
  stepdefinitions/positive/     Step definitions for the happy-path scenarios
  stepdefinitions/negative/     Step definitions for the error-path scenarios
src/test/resources/
  features/positive/            Happy-path Gherkin scenarios (pet, store, user)
  features/negative/            Negative/error-path Gherkin scenarios (pet, store, user)
  schemas/pet-schema.json       JSON Schema used to validate pet response bodies
  uploads/sample-photo.png      Fixture file used by the image-upload scenario
  config.properties             baseUri
  allure.properties             Allure results directory
  junit-platform.properties     Parallel execution settings
.github/workflows/ci.yml        Runs the suite on every push and pull request
```

## Prerequisites

- JDK 17+
- Maven 3.8+
- Internet access to `https://petstore.swagger.io`. The tests run against the live public demo server; there's no
  local service to start.
- (Optional, for a nicer local report) [Allure commandline](https://allurereport.org/docs/install/)

## Running the tests

```bash
mvn clean test
```

Point the suite at a different environment without touching the code:

```bash
mvn test -DbaseUri=https://your-petstore-instance/v2
```

Scenarios run in parallel (3 at a time by default, configured in `junit-platform.properties`), which brings the
full run down to well under a minute.

### Running a subset

Every scenario is tagged by resource (`@pet`, `@store`, `@user`) and outcome (`@positive`, `@negative`), and the
core CRUD scenarios are also tagged `@smoke`. Run just what you need with Cucumber's tag expressions:

```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@pet and @negative"
```

## Test reports

Every run produces two reports automatically, no extra steps required:

- **Cucumber HTML report**: `target/cucumber-report/cucumber.html` (open directly in a browser)
- **Allure results**: `target/allure-results` (raw results written on every run)

To view the richer Allure report:

```bash
mvn allure:serve      # builds the report and opens it in a browser
# or, without a live server:
mvn allure:report      # writes target/site/allure-maven-plugin/index.html
```

Every RestAssured call is attached to Allure as a request/response step, so a failing scenario shows exactly what
was sent and received.

## Continuous integration

`.github/workflows/ci.yml` runs the full suite on every push and pull request against `main`, and uploads the
Cucumber and Allure reports as build artifacts regardless of outcome.

## What's covered

39 scenarios across the three resource groups of the API, covering every endpoint in the Swagger Petstore spec.

**`/pet`** (`features/positive|negative/pet_*.feature`)
- Create, read, update (JSON and form-data), delete
- Search by status (single and multiple values), search by tag
- Image upload
- The created pet's response body is validated against `schemas/pet-schema.json`, not just individual fields
- Negative: non-existent pet on read/delete/form-update, non-numeric id on read/delete, malformed JSON body
- Edge cases: unknown status/tag returns an empty list rather than an error; `PUT` on an id that was never created
  acts as an upsert

**`/store`** (`features/positive|negative/store_*.feature`)
- Inventory lookup, place an order, read an order, delete an order
- Negative: non-existent order on read/delete, non-numeric order id, malformed JSON body

**`/user`** (`features/positive|negative/user_*.feature`)
- Create, read, update, delete, login (with rate-limit header check), logout
- Bulk create via `createWithArray` and `createWithList`
- Negative: non-existent user on read/delete, malformed JSON body on create/createWithArray/createWithList

Each scenario validates the HTTP status code, and where relevant, the response `Content-Type` header, response
headers (e.g. `X-Rate-Limit` on login), and body content (field values or error messages).

## Notes

- The public Petstore server is shared by everyone running this suite, so pet/order ids and usernames are
  randomised per run to avoid collisions, and `Hooks` best-effort deletes any pet, order or user (or users, for the
  bulk-create scenarios) a scenario created.
- The server is lenient about its own spec in most places. `POST /pet` without a `name` still returns 200,
  `PUT /pet` and `PUT /user/{username}` upsert rather than requiring the record to already exist, and
  `GET /user/login` "succeeds" for any username/password. Negative scenarios are built around behaviour the server
  actually enforces (malformed JSON bodies, non-existent resources, non-numeric path ids), each verified by hand
  against the live API before being automated.
- Malformed JSON is rejected differently depending on the endpoint: `400 "bad input"` on `/pet`, `/store/order` and
  `/user`, but `500 "something bad happened"` on `/user/createWithArray` and `/user/createWithList`. That's the
  real server's behaviour, not an inconsistency in the tests.
- The two "not found" error responses for orders differ in message casing depending on the HTTP verb
  (`"Order not found"` on GET vs. `"Order Not Found"` on DELETE), also the real server's behaviour, not a typo.
