package apitests.stepdefinitions.negative;

import apitests.client.OrderClient;
import apitests.support.TestContext;
import io.cucumber.java.en.When;

public class StoreNegativeSteps {

    private final TestContext testContext;
    private final OrderClient orderClient = new OrderClient();

    public StoreNegativeSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I request an order with id {long}")
    public void iRequestAnOrderWithId(long id) {
        testContext.setLastResponse(orderClient.getOrderById(id));
    }

    @When("I delete an order with id {long}")
    public void iDeleteAnOrderWithId(long id) {
        testContext.setLastResponse(orderClient.deleteOrderById(id));
    }
}
