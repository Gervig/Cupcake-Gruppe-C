package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.util.List;

public class UserController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.get("createuser", ctx -> ctx.render("createuser.html"));
        app.get("/shoppingBasket", ctx -> OrderController.checkOutCart(ctx, connectionPool));
        app.post("/shoppingBasket", ctx -> OrderController.checkOutCart(ctx, connectionPool));
        app.post("createuser", ctx -> createUser(ctx, connectionPool));
        app.get("listOfUsers", ctx -> listUsers(ctx, connectionPool));
        app.post("updateBalance", ctx -> updateUserBalance(ctx, connectionPool));
    }

    public static void updateUserBalance(Context ctx, ConnectionPool connectionPool) {
        int userId = Integer.parseInt(ctx.formParam("userId"));
        BigDecimal newBalance = new BigDecimal(ctx.formParam("newBalance"));

        try {
            UserMapper.updateUserBalance(userId, newBalance, connectionPool);
            ctx.attribute("message", "Balance updated successfully!");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Error updating balance: " + e.getMessage());
        }

        ctx.redirect("/listOfUsers"); // Redirect back to the user list after update
    }


    public static void listUsers(Context ctx, ConnectionPool connectionPool) {
        try {
            List<User> users = UserMapper.getAllUsers(connectionPool);
            ctx.attribute("users", users);
            ctx.render("listOfUsers.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Unable to retrieve users from the database.");
            ctx.render("error.html");
        }
    }

    public static void createUser(Context ctx, ConnectionPool connectionPool)
    {
        String username = ctx.formParam("username");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        String firstName = ctx.formParam("firstname");
        String lastName = ctx.formParam("lastname");
        String email = ctx.formParam("Email");
        String postcodeStr = ctx.formParam("postcode");

        int postcode;
        try
        {
            postcode = Integer.parseInt(postcodeStr);
        } catch (NumberFormatException e)
        {
            ctx.attribute("message", "Postnummer skal være et gyldigt tal. Prøv igen");
            ctx.render("createuser.html");
            return;
        }

        if (password1.equals(password2))
        {
            try
            {
                BigDecimal defaultBalance = new BigDecimal(500);

                UserMapper.createuser(username, password1, firstName, lastName, email, defaultBalance, postcode, connectionPool);

                ctx.attribute("message", "Du er hermed oprettet med brugernavn: " + username + ". Nu skal du logge på.");
                ctx.render("index.html");
            } catch (DatabaseException e)
            {
                ctx.attribute("message", "Dit brugernavn findes allerede. Prøv igen, eller log ind");
                ctx.render("createuser.html");
            }
        } else
        {
            ctx.attribute("message", "Dine to passwords matcher ikke! Prøv igen");
            ctx.render("createuser.html");
        }
    }


    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }


    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);

            if ("admin".equals(user.getRole())) {
                ctx.attribute("message", "Velkommen, Admin!");
                ctx.render("admin.html");
            } else {
                ctx.redirect("/orderCupcakes");  // Redirect to load cupcake data after login
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");
        }
    }

}
