# Cupcake-Gruppe-C

This project is created based on a presentation from a company that sells cupcakes. They wanted a website where customers could: 
- create a user
- log in
- order cupcakes
- see their previous orders

Furthermore they wanted the administrators (employees) to be able to:
- see all orders
- see all customers and their individuals orders and balance.
- update individual customers balance.

The design of the website is based on a mock up from the company.

For all this, we have used java Coretto17, javalin, postgresSQL, thymeleaf, HTML and CSS.
The informations regarding customers, orders and administrators are all stored in a database.

The HTMLs are all styled using CSS, and furthermore flexbox. Items on the HTML pages are gathered from the database via thymeleaf and the controllers. 
The mappers are used to gather the informations from the database and sessions.

Our motivation for this project is that it is a school project, and a challenge of our skills.

Some of our challenges have been to get the different controllers and mappers to work together, and be presented correctly via the HTMLs.


We have not been able to complete this project, and still needs to implement vital functions, such as actual buying the cupcakes, and not just put them into a basket and the order.
Furthermore we need to fix a problem with the function of ordering, as new users cannot order cupcakes, only already existing (hardcoded) users can.
There is also some styling for the HTML regarding shoppingBasket that needs improving, and pages regarding the toolbar on the HTML pages also needs fixing.

In the future we hope to implement the actual buying feature, and fix so that new users can order cupcakes.

To run this project one would need to establish a connection with a database named cupcake, and run the SQL sequence, before running the actual program. 
Then open up a browser, and then the program should be running.



Contributors:
Rikke Mariussen		cph-rm225@cphbusiness.dk	RikkeMariussen 
Casper Gervig		cph-cg201@cphbusiness.dk	Gervig 
Rasmus Højholt		cph-rs255@cphbusiness.dk	RHoejholt
Daniel Kroner		cph-dn138@cphbusiness.dk	DanielKroner 
Christoffer Leisted	cph-cl446@cphbusiness.dk	Crispy212
