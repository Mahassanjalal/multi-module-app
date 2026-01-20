-- Initial test data for Order Service
INSERT INTO orders (order_number, user_id, description, status, total_amount, shipping_address, created_at, updated_at)
VALUES ('ORD-20240115-00001', 1, 'First order from John Doe', 'DELIVERED', 159.97, '123 Main St, New York, NY', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO orders (order_number, user_id, description, status, total_amount, shipping_address, created_at, updated_at)
VALUES ('ORD-20240116-00002', 2, 'Order from Jane Smith', 'PROCESSING', 89.98, '456 Oak Ave, Los Angeles, CA', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO orders (order_number, user_id, description, status, total_amount, shipping_address, created_at, updated_at)
VALUES ('ORD-20240117-00003', 1, 'Second order from John Doe', 'PENDING', 299.99, '123 Main St, New York, NY', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO orders (order_number, user_id, description, status, total_amount, shipping_address, created_at, updated_at)
VALUES ('ORD-20240118-00004', 4, 'Order from Alice Williams', 'SHIPPED', 49.99, '321 Elm St, Houston, TX', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Order items for Order 1
INSERT INTO order_items (order_id, product_name, product_code, quantity, unit_price, total_price)
VALUES (1, 'Premium Widget', 'PROD-001', 2, 29.99, 59.98);

INSERT INTO order_items (order_id, product_name, product_code, quantity, unit_price, total_price)
VALUES (1, 'Deluxe Gadget', 'PROD-002', 1, 99.99, 99.99);

-- Order items for Order 2
INSERT INTO order_items (order_id, product_name, product_code, quantity, unit_price, total_price)
VALUES (2, 'Standard Widget', 'PROD-003', 2, 44.99, 89.98);

-- Order items for Order 3
INSERT INTO order_items (order_id, product_name, product_code, quantity, unit_price, total_price)
VALUES (3, 'Enterprise Solution', 'PROD-004', 1, 299.99, 299.99);

-- Order items for Order 4
INSERT INTO order_items (order_id, product_name, product_code, quantity, unit_price, total_price)
VALUES (4, 'Basic Widget', 'PROD-005', 1, 49.99, 49.99);
