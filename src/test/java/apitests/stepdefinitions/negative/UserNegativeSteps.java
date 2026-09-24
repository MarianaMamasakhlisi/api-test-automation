package apitests.stepdefinitions.negative;

import apitests.client.UserClient;
import apitests.support.TestContext;
import io.cucumber.java.en.When;

public class UserNegativeSteps {

    private final TestContext testContext;
    private final UserClient userClient = new UserClient();

    public UserNegativeSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I request a user with username {string}")
    public void iRequestAUserWithUsername(String username) {
        testContext.setLastResponse(userClient.getUserByUsername(username));
    }

    @When("I delete a user with username {string}")
    public void iDeleteAUserWithUsername(String username) {
        testContext.setLastResponse(userClient.deleteUserByUsername(username));
    }
}
