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

    @When("I send a malformed create user request")
    public void iSendAMalformedCreateUserRequest() {
        testContext.setLastResponse(userClient.createUserWithRawBody("{not-valid-json"));
    }

    @When("I send a malformed create-with-array request")
    public void iSendAMalformedCreateWithArrayRequest() {
        testContext.setLastResponse(userClient.createWithArrayRawBody("{not-an-array"));
    }

    @When("I send a malformed create-with-list request")
    public void iSendAMalformedCreateWithListRequest() {
        testContext.setLastResponse(userClient.createWithListRawBody("{not-a-list"));
    }
}
