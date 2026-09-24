# API Test Automation — Swagger Petstore

REST API test suite for the [Swagger Petstore](https://petstore.swagger.io/#/) demo API, built with Java, Maven,
RestAssured and Cucumber (Gherkin/BDD), run through the JUnit 5 platform.

## Stack

- Java 17, Maven
- Cucumber 7 (`cucumber-java`) + `cucumber-junit-platform-engine`
- RestAssured 5
- Jackson (for request/response (de)serialization)
- Allure + Cucumber's own HTML report

## Project layout

```
src/test/java/apitests/
  runner/RunCucumberTest.java   JUnit 5 suite entry point (@Suite, glue + plugin config)
  config/ApiConfig.java         Reads config.properties, allows -D overrides
  client/PetClient.java         RestAssured wrapper for the /pet endpoints
  models/                       Pet, Category, Tag request/response POJOs
  support/TestContext.java      Scenario-scoped state shared between steps and hooks
  hooks/Hooks.java              Sets the base URI; deletes any pet a scenario created
  stepdefinitions/PetCrudSteps.java
src/test/resources/
  features/pet_crud.feature     Gherkin scenarios
  config.properties             baseUri
  allure.properties             Allure results directory
```

## Prerequisites

- JDK 17+
- Maven 3.8+
- Internet access to `https://petstore.swagger.io` (the tests run against the live public demo server — no local
  service to start)
- (Optional, for a nicer local report) [Allure commandline](https://allurereport.org/docs/install/)

## Running the tests

```bash
mvn clean test
```

Point the suite at a different environment without touching the code:

```bash
mvn test -DbaseUri=https://your-petstore-instance/v2
```

## Test reports

Every run produces two reports automatically, no extra steps required:

- **Cucumber HTML report** — `target/cucumber-report/cucumber.html` (open directly in a browser)
- **Allure results** — `target/allure-results` (raw results written on every run)

To view the richer Allure report:

```bash
mvn allure:serve      # builds the report and opens it in a browser
# or, without a live server:
mvn allure:report      # writes target/site/allure-maven-plugin/index.html
```

Every RestAssured call is attached to Allure as a request/response step, so a failing scenario shows exactly what
was sent and received.

## What's covered

The `pet_crud.feature` file exercises the `/pet` resource end to end:

- **Create** — a new pet is created and the response echoes its name, status and content type.
- **Read** — a previously created pet can be fetched by id.
- **Update** — a pet's name and status can be changed, and the change is verified by reading it back.
- **Delete** — a pet can be removed, and fetching it afterwards returns 404.
- **Negative cases** — fetching or deleting a non-existent pet returns 404 with the expected error body; posting a
  malformed JSON body returns 400 with the expected error message.

Each scenario validates the HTTP status code, and where relevant, the response `Content-Type` header and body
content (field values or error messages).

## Notes

- The public Petstore server is shared by everyone running this suite, so pet ids are randomised per run to avoid
  collisions, and `Hooks` best-effort deletes any pet a scenario created.
- The server itself does not enforce the "required fields" from its own spec (e.g. `POST /pet` without a `name`
  still returns 200), so the negative scenarios target behaviour the server actually enforces: malformed JSON and
  non-existent resources.
