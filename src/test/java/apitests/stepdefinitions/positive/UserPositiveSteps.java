package apitests.stepdefinitions.positive;

import apitests.client.UserClient;
import apitests.models.User;
import apitests.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserPositiveSteps {

    private final TestContext testContext;
    private final UserClient userClient = new UserClient();

    public UserPositiveSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I create a user with a unique username")
    public void iCreateAUserWithAUniqueUsername() {
        User user = newUser();
        Response response = userClient.createUser(user);
        testContext.setLastResponse(response);
        testContext.setLastCreatedUser(user);
    }

    @Given("a user has been created")
    public void aUserHasBeenCreated() {
        iCreateAUserWithAUniqueUsername();
        testContext.getLastResponse().then().statusCode(200);
    }

    @When("I request that user by username")
    public void iRequestThatUserByUsername() {
        String username = testContext.getLastCreatedUser().getUsername();
        testContext.setLastResponse(userClient.getUserByUsername(username));
    }

    @When("I update that user's first name to {string}")
    public void iUpdateThatUsersFirstNameTo(String newFirstName) {
        User updated = testContext.getLastCreatedUser();
        updated.setFirstName(newFirstName);
        testContext.setLastResponse(userClient.updateUser(updated.getUsername(), updated));
    }

    @When("I delete that user")
    public void iDeleteThatUser() {
        String username = testContext.getLastCreatedUser().getUsername();
        testContext.setLastResponse(userClient.deleteUserByUsername(username));
    }

    @When("I log in as that user")
    public void iLogInAsThatUser() {
        User user = testContext.getLastCreatedUser();
        testContext.setLastResponse(userClient.login(user.getUsername(), user.getPassword()));
    }

    @When("I log out")
    public void iLogOut() {
        testContext.setLastResponse(userClient.logout());
    }

    @Then("the response user should have the same username")
    public void theResponseUserShouldHaveTheSameUsername() {
        User responseUser = testContext.getLastResponse().as(User.class);
        assertThat(responseUser.getUsername(), equalTo(testContext.getLastCreatedUser().getUsername()));
    }

    @Then("requesting that user again should return first name {string}")
    public void requestingThatUserAgainShouldReturnFirstName(String expectedFirstName) {
        String username = testContext.getLastCreatedUser().getUsername();
        User user = userClient.getUserByUsername(username).as(User.class);
        assertThat(user.getFirstName(), equalTo(expectedFirstName));
    }

    @Then("requesting that user again should return status code {int}")
    public void requestingThatUserAgainShouldReturnStatusCode(int expectedStatus) {
        String username = testContext.getLastCreatedUser().getUsername();
        Response response = userClient.getUserByUsername(username);
        assertThat(response.statusCode(), equalTo(expectedStatus));
    }

    @Then("the response should include a rate limit header")
    public void theResponseShouldIncludeARateLimitHeader() {
        assertThat(testContext.getLastResponse().getHeader("X-Rate-Limit"), notNullValue());
    }

    @When("I create multiple users using the array endpoint")
    public void iCreateMultipleUsersUsingTheArrayEndpoint() {
        List<User> users = List.of(newUser(), newUser());
        testContext.setLastResponse(userClient.createWithArray(users));
        testContext.setLastCreatedUsers(users);
    }

    @When("I create multiple users using the list endpoint")
    public void iCreateMultipleUsersUsingTheListEndpoint() {
        List<User> users = List.of(newUser(), newUser());
        testContext.setLastResponse(userClient.createWithList(users));
        testContext.setLastCreatedUsers(users);
    }

    private User newUser() {
        String username = "qa_user_" + ThreadLocalRandom.current().nextLong(100_000L, 999_999L);
        return User.builder()
                .id(ThreadLocalRandom.current().nextLong(100_000L, 999_999L))
                .username(username)
                .firstName("Test")
                .lastName("User")
                .email(username + "@example.com")
                .password("Pa55w0rd!")
                .phone("555-0100")
                .userStatus(1)
                .build();
    }
}
