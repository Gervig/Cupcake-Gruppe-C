package app.entities;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private BigDecimal totalPrice;
    private List<OrderLine> orderLines;

    public Order(int orderId, int userId, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderLines = orderLines;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
