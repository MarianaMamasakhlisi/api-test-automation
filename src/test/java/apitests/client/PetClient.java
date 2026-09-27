package apitests.client;

import apitests.config.ApiConfig;
import apitests.models.Pet;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.List;

/**
 * Thin wrapper around the /pet endpoints of the Swagger Petstore API.
 * Keeps RestAssured request-building out of the step definitions.
 */
public class PetClient {

    // baseUri is set per-request rather than on the shared static RestAssured config, so
    // scenarios stay independent of each other and can safely run in parallel.
    private RequestSpecification request() {
        return RestAssured.given()
                .baseUri(ApiConfig.baseUri())
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

    public Response deletePetById(String id) {
        return request().delete("/pet/{id}", id);
    }

    public Response findByStatus(String status) {
        return request().queryParam("status", status).get("/pet/findByStatus");
    }

    public Response findByStatus(List<String> statuses) {
        return request().queryParam("status", statuses).get("/pet/findByStatus");
    }

    public Response findByTags(String tag) {
        return request().queryParam("tags", tag).get("/pet/findByTags");
    }

    public Response updatePetWithForm(long id, String name, String status) {
        return request()
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", name)
                .formParam("status", status)
                .post("/pet/{id}", id);
    }

    public Response uploadImage(long id, File file) {
        return request()
                .contentType("multipart/form-data")
                .multiPart("file", file)
                .post("/pet/{id}/uploadImage", id);
    }
}
