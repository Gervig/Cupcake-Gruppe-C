package app.entities;

import java.math.BigDecimal;

public class Orderline
{
    private int orderlineId;
    private int orderId;
    private int bottomId;
    private int toppingId;
    private int quantity;
    private BigDecimal price;

    public Orderline(int orderlineId, int orderId, int bottomId, int toppingId, int quantity, BigDecimal price)
    {
        this.orderlineId = orderlineId;
        this.orderId = orderId;
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderlineId()
    {
        return orderlineId;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public int getBottomId()
    {
        return bottomId;
    }

    public int getToppingId()
    {
        return toppingId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
}
