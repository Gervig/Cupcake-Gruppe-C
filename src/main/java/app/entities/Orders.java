package app.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Orders
{
    private int orderId;
    private int userId;
    private Timestamp orderDate;
    private BigDecimal totalPrice;

    public Orders() {
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
