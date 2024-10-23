package app.entities;

import java.math.BigDecimal;

public class User
{
    private int userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private BigDecimal balance;
    private int postcode;
    private String role;

    public User(int userId, String firstName, String lastName, String userName, String email, String password, BigDecimal balance, int postcode, String role)
    {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.postcode = postcode;
        this.role = role;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }

    public String getRole()
    {
        return role;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public int getPostcode()
    {
        return postcode;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
