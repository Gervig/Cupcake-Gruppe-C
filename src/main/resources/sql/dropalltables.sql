-- Drop OrderLine table first (it references other tables)
DROP TABLE IF EXISTS OrderLine;

-- Drop Orders table (it references Customers)
DROP TABLE IF EXISTS Orders;

-- Drop Customers table (it references Cities)
DROP TABLE IF EXISTS Customers;

-- Drop Toppings and Bottoms tables (they don't have foreign key dependencies)
DROP TABLE IF EXISTS Toppings;
DROP TABLE IF EXISTS Bottoms;

-- Drop Cities table (it doesn't reference other tables)
DROP TABLE IF EXISTS Cities;