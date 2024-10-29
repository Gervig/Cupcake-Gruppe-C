package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public static void createuser(String userName, String password, String firstName, String lastName, String email, BigDecimal balance, int postcode, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "insert into users (username, password, first_name, last_name, email, balance, postcode) values (?,?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setString(5, email);
            ps.setBigDecimal(6, balance);
            ps.setInt(7, postcode);

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
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static List<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                BigDecimal balance = rs.getBigDecimal("balance");
                int postcode = rs.getInt("postcode");
                String role = rs.getString("role");

                User user = new User(id, username, firstName, lastName, email, null, balance, postcode, role);
                users.add(user);

            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching users from the database", e.getMessage());
        }
        return users;
    }
    public static void updateUserBalance(int userId, BigDecimal newBalance, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE users SET balance = ? WHERE user_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setBigDecimal(1, newBalance);
            ps.setInt(2, userId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Failed to update balance for user with ID: " + userId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while updating balance", e.getMessage());
        }
    }



}