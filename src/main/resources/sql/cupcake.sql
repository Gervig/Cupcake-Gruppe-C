-- Step 1: Drop all tables in the correct order (do this manually as per your preference)

-- Step 2: Create Cities Table
CREATE TABLE Cities
(
    postcode  INT PRIMARY KEY CHECK (postcode >= 1000 AND postcode <= 9999), -- Ensure postcode is a 4-digit integer
    city_name VARCHAR(255) NOT NULL                                          -- City name as VARCHAR (string)
);

-- Inserting data into the Cities table
-- Insert Danish cities and their postcodes into the Cities table
INSERT INTO Cities (postcode, city_name)
VALUES (1050, 'København K'),
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
       (9000, 'Aalborg');

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

-- Step 3: Create Customers Table (with Role, Username, Email, Password, and Balance)
CREATE TABLE Customers
(
    customer_id SERIAL PRIMARY KEY,                              -- Auto-incrementing ID for customers
    first_name  VARCHAR(255)        NOT NULL,                    -- Customer's first name as VARCHAR (string)
    last_name   VARCHAR(255)        NOT NULL,                    -- Customer's last name as VARCHAR (string)
    postcode    INT REFERENCES Cities (postcode),                -- Reference to the Cities table with 4-digit check
    role        VARCHAR(255)        NOT NULL DEFAULT 'customer', -- Role column with default value 'customer'
    username    VARCHAR(255) UNIQUE NOT NULL,                    -- Username must be unique
    email       VARCHAR(255) UNIQUE NOT NULL,                    -- Email must be unique
    password    VARCHAR(255)        NOT NULL,                    -- Password for customer login
    balance     NUMERIC(10, 2)      NOT NULL DEFAULT 0.00        -- Account balance with 2 decimal places, default to 0.00
);

-- Inserting sample data into the Customers table
INSERT INTO Customers (first_name, last_name, postcode, role, username, email, password, balance)
VALUES ('John', 'Doe', 1234, 'customer', 'john_doe', 'john@example.com', 'hashed_password_1', 50.00),
       ('Jane', 'Smith', 5432, 'customer', 'jane_smith', 'jane@example.com', 'hashed_password_2', 75.00),
       ('Casper', 'Gervig', 2400, 'customer', 'casper', 'gervig91@gmail.com', '1234', 1000.00),
       ('Casper', 'Gervig', 2400, 'admin', 'casper_admin', 'cph-cg201@cphbusiness.dk', '1234', 0.00);

-- Step 4: Create Orders Table
CREATE TABLE Orders
(
    order_id    SERIAL PRIMARY KEY,                    -- Auto-incrementing ID for orders
    order_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- Order date with default value as current timestamp
    customer_id INT REFERENCES Customers (customer_id) -- Reference to the Customers table
);

-- Step 5: Create Toppings Table (with Price as NUMERIC)
CREATE TABLE Toppings
(
    topping_id   SERIAL PRIMARY KEY,                       -- Auto-incrementing ID for toppings
    topping_name VARCHAR(255)  NOT NULL,                   -- Name of the topping as VARCHAR (string)
    price        NUMERIC(5, 2) NOT NULL CHECK (price >= 0) -- Price of the topping as NUMERIC with 2 decimal places
);

-- Step 6: Create Bottoms Table (with Price as NUMERIC)
CREATE TABLE Bottoms
(
    bottom_id   SERIAL PRIMARY KEY,                       -- Auto-incrementing ID for bottoms
    bottom_name VARCHAR(255)  NOT NULL,                   -- Name of the bottom type as VARCHAR (string)
    price       NUMERIC(5, 2) NOT NULL CHECK (price >= 0) -- Price of the bottom as NUMERIC with 2 decimal places
);

-- Step 7: Create OrderLine Table
CREATE TABLE OrderLine
(
    orderline_id SERIAL PRIMARY KEY,                                 -- Auto-incrementing ID for each order line (cupcake)
    order_id     INT REFERENCES Orders (order_id) ON DELETE CASCADE, -- Reference to the Orders table
    topping_id   INT REFERENCES Toppings (topping_id),               -- Reference to the Toppings table
    bottom_id    INT REFERENCES Bottoms (bottom_id),                 -- Reference to the Bottoms table
    quantity     INT NOT NULL CHECK (quantity > 0)                   -- Quantity of the same cupcake, must be positive
);

-- Inserting data into the Bottoms table
INSERT INTO Bottoms (bottom_name, price)
VALUES ('Chocolate', 5.00),
       ('Vanilla', 5.00),
       ('Nutmeg', 5.00),
       ('Pistacio', 6.00),
       ('Almond', 7.00);

-- Inserting data into the Toppings table
INSERT INTO Toppings (topping_name, price)
VALUES ('Chocolate', 5.00),
       ('Blueberry', 5.00),
       ('Rasberry', 5.00),
       ('Crispy', 6.00),
       ('Strawberry', 6.00),
       ('Rum/Raisin', 7.00),
       ('Orange', 8.00),
       ('Lemon', 8.00),
       ('Blue cheese', 9.00);
