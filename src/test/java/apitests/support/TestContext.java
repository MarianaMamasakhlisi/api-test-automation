package apitests.support;

import apitests.models.Order;
import apitests.models.Pet;
import apitests.models.User;
import io.restassured.response.Response;

import java.util.List;

/**
 * Scenario-scoped state shared between step definitions and hooks.
 * Cucumber creates a fresh instance (via picocontainer) for every scenario.
 */
public class TestContext {

    private Response lastResponse;
    private Pet lastCreatedPet;
    private Order lastCreatedOrder;
    private User lastCreatedUser;
    private List<User> lastCreatedUsers;

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public Pet getLastCreatedPet() {
        return lastCreatedPet;
    }

    public void setLastCreatedPet(Pet lastCreatedPet) {
        this.lastCreatedPet = lastCreatedPet;
    }

    public Order getLastCreatedOrder() {
        return lastCreatedOrder;
    }

    public void setLastCreatedOrder(Order lastCreatedOrder) {
        this.lastCreatedOrder = lastCreatedOrder;
    }

    public User getLastCreatedUser() {
        return lastCreatedUser;
    }

    public void setLastCreatedUser(User lastCreatedUser) {
        this.lastCreatedUser = lastCreatedUser;
    }

    public List<User> getLastCreatedUsers() {
        return lastCreatedUsers;
    }

    public void setLastCreatedUsers(List<User> lastCreatedUsers) {
        this.lastCreatedUsers = lastCreatedUsers;
    }
}
