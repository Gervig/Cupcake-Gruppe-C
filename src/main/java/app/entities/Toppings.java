package app.entities;

public class Toppings
{
    private int toppingsID;
    private String toppingsName;
    private float price;

    public Toppings(int toppingsID, String toppingsName, float price)
    {
        this.toppingsID = toppingsID;
        this.toppingsName = toppingsName;
        this.price = price;
    }

    public int getToppingsID()
    {
        return toppingsID;
    }

    public String getToppingsName()
    {
        return toppingsName;
    }

    public float getPrice()
    {
        return price;
    }
}
