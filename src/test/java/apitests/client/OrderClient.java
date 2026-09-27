package apitests.client;

import apitests.config.ApiConfig;
import apitests.models.Order;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class OrderClient {

    private RequestSpecification request() {
        return RestAssured.given()
                .baseUri(ApiConfig.baseUri())
                .filter(new AllureRestAssured())
                .contentType("application/json");
    }

    public Response getInventory() {
        return request().get("/store/inventory");
    }

    public Response placeOrder(Order order) {
        return request().body(order).post("/store/order");
    }

    public Response placeOrderWithRawBody(String rawJson) {
        return request().body(rawJson).post("/store/order");
    }

    public Response getOrderById(long id) {
        return request().get("/store/order/{id}", id);
    }

    public Response getOrderById(String id) {
        return request().get("/store/order/{id}", id);
    }

    public Response deleteOrderById(long id) {
        return request().delete("/store/order/{id}", id);
    }

    public Response deleteOrderById(String id) {
        return request().delete("/store/order/{id}", id);
    }
}
