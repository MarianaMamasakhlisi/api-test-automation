package apitests.stepdefinitions.common;

import apitests.support.TestContext;
import io.cucumber.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Assertions that apply equally to positive and negative scenarios
 * (response status code, content type) live here so they aren't duplicated.
 */
public class CommonSteps {

    private final TestContext testContext;

    public CommonSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatus) {
        assertThat(testContext.getLastResponse().statusCode(), equalTo(expectedStatus));
    }

    @Then("the response content type should be {string}")
    public void theResponseContentTypeShouldBe(String expectedContentType) {
        assertThat(testContext.getLastResponse().contentType(), containsString(expectedContentType));
    }

    @Then("the response body message should contain {string}")
    public void theResponseBodyMessageShouldContain(String expectedFragment) {
        String actualMessage = testContext.getLastResponse().jsonPath().getString("message");
        assertThat(actualMessage, containsString(expectedFragment));
    }
}
