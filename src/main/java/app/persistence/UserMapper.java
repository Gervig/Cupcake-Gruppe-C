package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper
{

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "SELECT * FROM public.\"users\" WHERE LOWER(email) = LOWER(?) AND password = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String userName = rs.getString("username");
                BigDecimal balance = rs.getBigDecimal("balance");
                int postcode = rs.getInt("postcode");
                String role = rs.getString("role");
                return new User(id, firstName, lastName, userName, email, password, balance, postcode, role);
            } else
            {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static void createuser(String firstName, String lastName, String userName, String email, String password, int postcode, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "insert into users (first_name, last_name, username, email, password, balance, postcode, role) values (?,?,?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, userName);
            ps.setString(4, email);
            ps.setString(5, password);
            ps.setInt(6, postcode);
            ps.setBigDecimal(7, BigDecimal.valueOf(500.00));
            ps.setString(8, "customer");

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        } catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "Brugernavn eller email findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }
}