package app.entities;

import java.math.BigDecimal;

public class Toppings
{
    private int toppingsID;
    private String toppingsName;
    private BigDecimal price;

    public Toppings(int toppingsID, String toppingsName, BigDecimal price)
    {
        this.toppingsID = toppingsID;
        this.toppingsName = toppingsName;
        this.price = price;
    }

    public int getToppingsID()
    {
        return toppingsID;
    }

    public String getToppingName()
    {
        return toppingsName;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
}
