package apitests.stepdefinitions.positive;

import apitests.client.OrderClient;
import apitests.models.Order;
import apitests.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class StorePositiveSteps {

    private final TestContext testContext;
    private final OrderClient orderClient = new OrderClient();

    public StorePositiveSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I request the store inventory")
    public void iRequestTheStoreInventory() {
        testContext.setLastResponse(orderClient.getInventory());
    }

    @Then("the inventory should report a count for status {string}")
    public void theInventoryShouldReportACountForStatus(String status) {
        Integer count = testContext.getLastResponse().jsonPath().getInt(status);
        assertThat(count, greaterThanOrEqualTo(0));
    }

    @When("I place an order for pet id {long} with quantity {int}")
    public void iPlaceAnOrderForPetIdWithQuantity(long petId, int quantity) {
        Order order = newOrder(petId, quantity);
        Response response = orderClient.placeOrder(order);
        testContext.setLastResponse(response);
        testContext.setLastCreatedOrder(order);
    }

    @Given("an order for pet id {long} with quantity {int} has been placed")
    public void anOrderForPetIdWithQuantityHasBeenPlaced(long petId, int quantity) {
        iPlaceAnOrderForPetIdWithQuantity(petId, quantity);
        testContext.getLastResponse().then().statusCode(200);
    }

    @When("I request that order by its id")
    public void iRequestThatOrderByItsId() {
        long id = testContext.getLastCreatedOrder().getId();
        testContext.setLastResponse(orderClient.getOrderById(id));
    }

    @When("I delete that order")
    public void iDeleteThatOrder() {
        long id = testContext.getLastCreatedOrder().getId();
        testContext.setLastResponse(orderClient.deleteOrderById(id));
    }

    @Then("the response order should have quantity {int}")
    public void theResponseOrderShouldHaveQuantity(int expectedQuantity) {
        Order order = testContext.getLastResponse().as(Order.class);
        assertThat(order.getQuantity(), equalTo(expectedQuantity));
    }

    @Then("requesting that order again should return status code {int}")
    public void requestingThatOrderAgainShouldReturnStatusCode(int expectedStatus) {
        long id = testContext.getLastCreatedOrder().getId();
        Response response = orderClient.getOrderById(id);
        assertThat(response.statusCode(), equalTo(expectedStatus));
    }

    private Order newOrder(long petId, int quantity) {
        long id = uniqueOrderId();
        return Order.builder()
                .id(id)
                .petId(petId)
                .quantity(quantity)
                .shipDate(Instant.now().toString())
                .status("placed")
                .complete(true)
                .build();
    }

    // Keeps concurrent runs from colliding on the same order record on the shared demo server.
    private long uniqueOrderId() {
        return ThreadLocalRandom.current().nextLong(1_000_00L, 999_999L);
    }
}
