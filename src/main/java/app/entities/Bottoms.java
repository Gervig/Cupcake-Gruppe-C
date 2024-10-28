package app.entities;

public class Bottoms
{
    private int buttomsID;
    private String bottomsName;
    private float price;

    public Bottoms(int buttomsID, String bottomsName, float price)
    {
        this.buttomsID = buttomsID;
        this.bottomsName = bottomsName;
        this.price = price;
    }

    public int getButtomsID()
    {
        return buttomsID;
    }

    public String getBottomsName()
    {
        return bottomsName;
    }

    public float getPrice()
    {
        return price;
    }
}
