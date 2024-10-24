package app.entities;

import java.math.BigDecimal;

public class OrderLine {
    private int orderLineId;
    private int orderId;
    private int bottomId;
    private int toppingId;
    private int quantity;
    private BigDecimal price;

    public OrderLine(int orderLineId, int orderId, int bottomId, int toppingId, int quantity, BigDecimal price) {
        this.orderLineId = orderLineId;
        this.orderId = orderId;
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderLineId() {
        return orderLineId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public int getToppingId() {
        return toppingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setOrderLineId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
