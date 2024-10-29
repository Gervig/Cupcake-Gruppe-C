package app.entities;

import java.math.BigDecimal;

public class Bottoms
{
    private int bottomsID;
    private String bottomsName;
    private BigDecimal price;

    public Bottoms(int bottomsID, String bottomsName, BigDecimal price)
    {
        this.bottomsID = bottomsID;
        this.bottomsName = bottomsName;
        this.price = price;
    }

    public int getBottomsID()
    {
        return bottomsID;
    }

    public String getBottomName()
    {
        return bottomsName;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
}
