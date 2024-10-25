SELECT o.order_id, o.order_date, o.total_price,
       ol.orderline_id, ol.quantity, ol.price AS orderline_price,
       b.bottom_name, b.price AS bottom_price,
       t.topping_name, t.price AS topping_price,
       u.user_id, u.first_name, u.last_name, u.email
FROM orders o
         JOIN orderline ol ON o.order_id = ol.order_id
         JOIN users u ON o.user_id = u.user_id
         JOIN bottom b ON ol.bottom_id = b.bottom_id
         JOIN topping t ON ol.topping_id = t.topping_id;
