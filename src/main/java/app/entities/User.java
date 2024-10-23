package app.entities;

import java.math.BigDecimal;

public class User
{
    private int userId;
    private String userName;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal balance;
    private int postcode;

    public User(int userId, String firstName, String lastName, String userName, String email, String password, int postcode, String role)
    {
        balance = BigDecimal.valueOf(500.00);
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.balance = balance;
        this.postcode = postcode;
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
