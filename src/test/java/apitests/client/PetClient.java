package apitests.client;

import apitests.models.Pet;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Thin wrapper around the /pet endpoints of the Swagger Petstore API.
 * Keeps RestAssured request-building out of the step definitions.
 */
public class PetClient {

    private RequestSpecification request() {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .contentType("application/json");
    }

    public Response createPet(Pet pet) {
        return request().body(pet).post("/pet");
    }

    public Response createPetWithRawBody(String rawJson) {
        return request().body(rawJson).post("/pet");
    }

    public Response getPetById(long id) {
        return request().get("/pet/{id}", id);
    }

    public Response getPetById(String id) {
        return request().get("/pet/{id}", id);
    }

    public Response updatePet(Pet pet) {
        return request().body(pet).put("/pet");
    }

    public Response deletePetById(long id) {
        return request().delete("/pet/{id}", id);
    }

    public Response findByStatus(String status) {
        return request().queryParam("status", status).get("/pet/findByStatus");
    }
}
