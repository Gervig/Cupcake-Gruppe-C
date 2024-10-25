SELECT o.order_id, o.order_date, o.total_price,
       ol.orderline_id, ol.bottom_id, ol.topping_id, ol.quantity, ol.price AS orderline_price,
       u.user_id, u.first_name, u.last_name
FROM orders o
         JOIN orderline ol ON o.order_id = ol.order_id
         JOIN users u ON o.user_id = u.user_id
WHERE u.user_id = :userId;
