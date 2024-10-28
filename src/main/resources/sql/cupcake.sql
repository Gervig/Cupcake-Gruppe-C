-- Drop tables if they exist (this should be run in the correct order due to dependencies)
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
-- Create the City table
CREATE TABLE City
(
    postcode  INT PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL
);

-- Create the Bottom table
CREATE TABLE Bottom
(
    bottom_id   SERIAL PRIMARY KEY,
    bottom_name VARCHAR(50)   NOT NULL,
    price       NUMERIC(5, 2) NOT NULL -- Price with two decimal points
);

-- Create the Topping table
CREATE TABLE Topping
(
    topping_id   SERIAL PRIMARY KEY,
    topping_name VARCHAR(50)   NOT NULL,
    price        NUMERIC(5, 2) NOT NULL -- Price with two decimal points
);

-- Insert Danish cities and postcodes, including Bornholm cities
INSERT INTO City (postcode, city_name)
VALUES (1000, 'København K'),
       (1050, 'København K'),
       (1300, 'København K'),
       (2000, 'Frederiksberg'),
       (2100, 'København Ø'),
       (2200, 'København N'),
       (2300, 'København S'),
       (2400, 'København NV'),
       (2500, 'Valby'),
       (2600, 'Glostrup'),
       (2620, 'Albertslund'),
       (2630, 'Taastrup'),
       (2650, 'Hvidovre'),
       (2700, 'Brønshøj'),
       (2800, 'Kongens Lyngby'),
       (2820, 'Gentofte'),
       (2900, 'Hellerup'),
       (2920, 'Charlottenlund'),
       (3000, 'Helsingør'),
       (3050, 'Humlebæk'),
       (3100, 'Hornbæk'),
       (3400, 'Hillerød'),
       (3450, 'Allerød'),
       (3460, 'Birkerød'),
       (3500, 'Værløse'),
       (3600, 'Frederikssund'),
       (4000, 'Roskilde'),
       (4100, 'Ringsted'),
       (4200, 'Slagelse'),
       (4300, 'Holbæk'),
       (4400, 'Kalundborg'),
       (5000, 'Odense C'),
       (5100, 'Odense M'),
       (5200, 'Odense V'),
       (6000, 'Kolding'),
       (6700, 'Esbjerg'),
       (7100, 'Vejle'),
       (8000, 'Aarhus C'),
       (8200, 'Aarhus N'),
       (8260, 'Viby J'),
       (9000, 'Aalborg'),
-- Bornholm Cities
       (3700, 'Rønne'),
       (3720, 'Aakirkeby'),
       (3730, 'Nexø'),
       (3740, 'Svaneke'),
       (3751, 'Østermarie'),
       (3760, 'Gudhjem'),
       (3770, 'Allinge'),
       (3782, 'Klemensker'),
       (3790, 'Hasle');

-- Create the User table (formerly Customers)
CREATE TABLE Users
(
    user_id    SERIAL PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    username   VARCHAR(50)  NOT NULL UNIQUE,      -- Unique usernames
    email      VARCHAR(100) NOT NULL UNIQUE,      -- Unique emails
    password   VARCHAR(255) NOT NULL,
    balance    NUMERIC(10, 2) DEFAULT 0.00,       -- Balance on user's account
    postcode   INT          NOT NULL,             -- Foreign key referencing City
    role       VARCHAR(20)    DEFAULT 'customer', -- Default role is 'customer'
    FOREIGN KEY (postcode) REFERENCES City (postcode)
);

-- Create the Order table
CREATE TABLE Orders
(
    order_id    SERIAL PRIMARY KEY,
    user_id     INT NOT NULL,                             -- Foreign key referencing User
    order_date  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP, -- Order creation date
    total_price NUMERIC(10, 2) DEFAULT 0.00,              -- Total price of the order
    FOREIGN KEY (user_id) REFERENCES Users (user_id)
);

-- Create the Orderline table
CREATE TABLE Orderline
(
    orderline_id SERIAL PRIMARY KEY,
    order_id     INT            NOT NULL, -- Foreign key referencing Order
    bottom_id    INT            NOT NULL, -- Foreign key referencing Bottom
    topping_id   INT            NOT NULL, -- Foreign key referencing Topping
    quantity     INT            NOT NULL, -- Quantity of the specific cupcake
    price        NUMERIC(10, 2) NOT NULL, -- Price for this orderline
    FOREIGN KEY (order_id) REFERENCES Orders (order_id),
    FOREIGN KEY (bottom_id) REFERENCES Bottom (bottom_id),
    FOREIGN KEY (topping_id) REFERENCES Topping (topping_id)
);

-- Inserting sample data into the User table
INSERT INTO Users (first_name, last_name, postcode, role, username, email, password, balance)
VALUES ('Casper', 'Gervig', 2400, 'customer', 'casper', 'gervig91@gmail.com', '1234', 1000.00),
       ('Casper', 'Gervig', 2400, 'admin', 'casper_admin', 'cph-cg201@cphbusiness.dk', '1234', 0.00);


-- Insert data into Bottom table
INSERT INTO Bottom (bottom_name, price)
VALUES ('Chocolate', 5.00),
       ('Vanilla', 5.00),
       ('Nutmeg', 5.00),
       ('Pistachio', 6.00),
       ('Almond', 7.00);

-- Insert data into Topping table
INSERT INTO Topping (topping_name, price)
VALUES ('Chocolate', 5.00),
       ('Blueberry', 5.00),
       ('Raspberry', 5.00),
       ('Crispy', 6.00),
       ('Strawberry', 6.00),
       ('Rum/Raisin', 7.00),
       ('Orange', 8.00),
       ('Lemon', 8.00),
       ('Blue cheese', 9.00);

-- creates a test schema for our test class
DROP SCHEMA IF EXISTS test_schema CASCADE;

-- Create the test_schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS test_schema;

-- Create the City table in the test_schema
CREATE TABLE IF NOT EXISTS test_schema.City
(
    postcode  INTEGER PRIMARY KEY CHECK (postcode >= 1000 AND postcode <= 9999),
    city_name VARCHAR(50) NOT NULL
);

-- Create the Users table in the test_schema
CREATE TABLE IF NOT EXISTS test_schema.Users
(
    user_id    SERIAL PRIMARY KEY,
    first_name VARCHAR(50)         NOT NULL,
    last_name  VARCHAR(50)         NOT NULL,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    balance    NUMERIC(10, 2) DEFAULT 0.00,
    postcode   INTEGER             REFERENCES test_schema.City (postcode) ON DELETE SET NULL,
    role       VARCHAR(20)         NOT NULL CHECK (role IN ('customer', 'admin'))
);

-- Create the Bottom table in the test_schema
CREATE TABLE IF NOT EXISTS test_schema.Bottom
(
    bottom_id   SERIAL PRIMARY KEY,
    bottom_name VARCHAR(50)   NOT NULL,
    price       NUMERIC(5, 2) NOT NULL
);

-- Create the Topping table in the test_schema
CREATE TABLE IF NOT EXISTS test_schema.Topping
(
    topping_id   SERIAL PRIMARY KEY,
    topping_name VARCHAR(50)   NOT NULL,
    price        NUMERIC(5, 2) NOT NULL
);

-- Create the Orders table in the test_schema
CREATE TABLE IF NOT EXISTS test_schema.Orders
(
    order_id    SERIAL PRIMARY KEY,
    user_id     INTEGER REFERENCES test_schema.Users (user_id) ON DELETE CASCADE,
    order_date  TIMESTAMP DEFAULT NOW(),
    total_price NUMERIC(10, 2) NOT NULL
);

-- Create the Orderline table in the test_schema
CREATE TABLE IF NOT EXISTS test_schema.Orderline
(
    orderline_id SERIAL PRIMARY KEY,
    order_id     INTEGER REFERENCES test_schema.Orders (order_id) ON DELETE CASCADE,
    bottom_id    INTEGER        REFERENCES test_schema.Bottom (bottom_id) ON DELETE SET NULL,
    topping_id   INTEGER        REFERENCES test_schema.Topping (topping_id) ON DELETE SET NULL,
    quantity     INTEGER        NOT NULL CHECK (quantity > 0),
    price        NUMERIC(10, 2) NOT NULL
);
