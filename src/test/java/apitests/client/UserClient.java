package apitests.client;

import apitests.models.User;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Thin wrapper around the /user endpoints of the Swagger Petstore API.
 */
public class UserClient {

    private RequestSpecification request() {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .contentType("application/json");
    }

    public Response createUser(User user) {
        return request().body(user).post("/user");
    }

    public Response getUserByUsername(String username) {
        return request().get("/user/{username}", username);
    }

    public Response updateUser(String username, User user) {
        return request().body(user).put("/user/{username}", username);
    }

    public Response deleteUserByUsername(String username) {
        return request().delete("/user/{username}", username);
    }

    public Response login(String username, String password) {
        return request()
                .queryParam("username", username)
                .queryParam("password", password)
                .get("/user/login");
    }

    public Response logout() {
        return request().get("/user/logout");
    }
}
