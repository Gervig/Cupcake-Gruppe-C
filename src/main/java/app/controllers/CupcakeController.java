package app.controllers;

import app.persistence.ConnectionPool;
import app.persistence.CupcakeService;
import io.javalin.Javalin;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

public class CupcakeController
{

    private CupcakeService cupcakeService;

    public CupcakeController(CupcakeService cupcakeService)
    {
        this.cupcakeService = cupcakeService;
    }

    public void registerRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.get("/", ctx ->
        {
            TemplateEngine templateEngine = new TemplateEngine();
            WebContext webContext = new WebContext(ctx.req, ctx.res, ctx.req.getServletContext());

            webContext.setVariable("bottoms", cupcakeService.getBottoms(connectionPool));
            webContext.setVariable("toppings", cupcakeService.getToppings(connectionPool));

            templateEngine.process("index.html", webContext, ctx.res.getWriter());
        });

        app.post("/order", ctx ->
        {
            String bottom = ctx.formParam("bottom");
            String topping = ctx.formParam("topping");

            cupcakeService.createOrder(Integer.parseInt(bottom), Integer.parseInt(topping), connectionPool);

            ctx.redirect("/confirmation");
        });

        app.get("/confirmation", ctx ->
        {
            ctx.render("confirmation.html");
        });
    }
}
