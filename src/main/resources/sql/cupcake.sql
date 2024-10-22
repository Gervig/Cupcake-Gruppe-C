-- Step 1: Create Cities Table
CREATE TABLE Cities
(
    postcode  INT PRIMARY KEY,      -- Postcode is unique for each city
    city_name VARCHAR(255) NOT NULL -- City name as VARCHAR (string)
);

-- Step 2: Create Customers Table (with Role)
CREATE TABLE Customers
(
    customer_id SERIAL PRIMARY KEY,                      -- Auto-incrementing ID for customers
    first_name  VARCHAR(255) NOT NULL,                   -- Customer's first name as VARCHAR (string)
    last_name   VARCHAR(255) NOT NULL,                   -- Customer's last name as VARCHAR (string)
    postcode    INT REFERENCES Cities (postcode),-- Reference to the Cities table
    role        VARCHAR(255) NOT NULL DEFAULT 'customer' -- Role column with default value 'customer'
);

-- Step 3: Create Orders Table
CREATE TABLE Orders
(
    order_id    SERIAL PRIMARY KEY,                    -- Auto-incrementing ID for orders
    order_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- Order date with default value as current timestamp
    customer_id INT REFERENCES Customers (customer_id) -- Reference to the Customers table
);

-- Step 4: Create Toppings Table (with Price as NUMERIC)
CREATE TABLE Toppings
(
    topping_id   SERIAL PRIMARY KEY,                       -- Auto-incrementing ID for toppings
    topping_name VARCHAR(255)  NOT NULL,                   -- Name of the topping as VARCHAR (string)
    price        NUMERIC(5, 2) NOT NULL CHECK (price >= 0) -- Price of the topping as NUMERIC with 2 decimal places
);

-- Step 5: Create Bottoms Table (with Price as NUMERIC)
CREATE TABLE Bottoms
(
    bottom_id   SERIAL PRIMARY KEY,                       -- Auto-incrementing ID for bottoms
    bottom_name VARCHAR(255)  NOT NULL,                   -- Name of the bottom type as VARCHAR (string)
    price       NUMERIC(5, 2) NOT NULL CHECK (price >= 0) -- Price of the bottom as NUMERIC with 2 decimal places
);

-- Step 6: Create OrderLine Table
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
