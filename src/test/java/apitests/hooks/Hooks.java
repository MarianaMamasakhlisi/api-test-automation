package apitests.hooks;

import apitests.client.OrderClient;
import apitests.client.PetClient;
import apitests.client.UserClient;
import apitests.config.ApiConfig;
import apitests.support.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {

    private final TestContext testContext;
    private final PetClient petClient = new PetClient();
    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = ApiConfig.baseUri();
    }

    // Best-effort cleanup so scenarios don't leave test data behind on the shared demo server.
    // A record already removed by the scenario itself (e.g. the delete tests) simply 404s here,
    // which is fine.
    @After
    public void cleanUp() {
        if (testContext.getLastCreatedPet() != null && testContext.getLastCreatedPet().getId() != null) {
            petClient.deletePetById(testContext.getLastCreatedPet().getId());
        }
        if (testContext.getLastCreatedOrder() != null && testContext.getLastCreatedOrder().getId() != null) {
            orderClient.deleteOrderById(testContext.getLastCreatedOrder().getId());
        }
        if (testContext.getLastCreatedUser() != null && testContext.getLastCreatedUser().getUsername() != null) {
            userClient.deleteUserByUsername(testContext.getLastCreatedUser().getUsername());
        }
        if (testContext.getLastCreatedUsers() != null) {
            testContext.getLastCreatedUsers().forEach(user -> userClient.deleteUserByUsername(user.getUsername()));
        }
    }
}
