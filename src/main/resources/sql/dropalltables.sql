-- THIS DELETES THE DB WEE WOO DANGERZONE
-- Drop OrderLine table first (it references other tables)
DROP TABLE IF EXISTS OrderLine;

-- Drop Orders table (it references Customers)
DROP TABLE IF EXISTS Orders;

-- Drop Customers table (it references Cities)
DROP TABLE IF EXISTS Users;

-- Drop Toppings and Bottoms tables (they don't have foreign key dependencies)
DROP TABLE IF EXISTS Topping;
DROP TABLE IF EXISTS Bottom;

-- Drop Cities table (it doesn't reference other tables)
DROP TABLE IF EXISTS City;