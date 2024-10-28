package app.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Orders {
    private int orderId;
    private int userId;
    private Timestamp orderDate;
    private BigDecimal totalPrice;

    public Orders(int orderId, int userId, Timestamp orderDate, BigDecimal totalPrice)
    {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public int getUserId()
    {
        return userId;
    }

    public Timestamp getOrderDate()
    {
        return orderDate;
    }

    public BigDecimal getTotalPrice()
    {
        return totalPrice;
    }
}
