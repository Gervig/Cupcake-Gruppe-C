-- Administrators can view all customers and their orders for tracking purposes.
SELECT
    c.customer_id,
    c.first_name,
    c.last_name,
    c.username,
    c.email,
    c.balance,
    o.order_id,
    o.order_date
FROM
    Customers c
        LEFT JOIN
    Orders o ON c.customer_id = o.customer_id
ORDER BY
    c.customer_id, o.order_date DESC;
