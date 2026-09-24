package apitests.models;

public class Order {

    private Long id;
    private Long petId;
    private Integer quantity;
    private String shipDate;
    private String status;
    private Boolean complete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Order order = new Order();

        public Builder id(long id) {
            order.setId(id);
            return this;
        }

        public Builder petId(long petId) {
            order.setPetId(petId);
            return this;
        }

        public Builder quantity(int quantity) {
            order.setQuantity(quantity);
            return this;
        }

        public Builder shipDate(String shipDate) {
            order.setShipDate(shipDate);
            return this;
        }

        public Builder status(String status) {
            order.setStatus(status);
            return this;
        }

        public Builder complete(boolean complete) {
            order.setComplete(complete);
            return this;
        }

        public Order build() {
            return order;
        }
    }
}
