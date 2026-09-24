package apitests.stepdefinitions.negative;

import apitests.support.TestContext;
import io.cucumber.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Error-body assertion shared by pet, order and user negative scenarios.
 */
public class ErrorResponseSteps {

    private final TestContext testContext;

    public ErrorResponseSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("the response body message should be {string}")
    public void theResponseBodyMessageShouldBe(String expectedMessage) {
        String actualMessage = testContext.getLastResponse().jsonPath().getString("message");
        assertThat(actualMessage, equalTo(expectedMessage));
    }
}
