-- Initial test data for User Service
INSERT INTO users (first_name, last_name, email, phone, address, status, created_at, updated_at)
VALUES ('John', 'Doe', 'john.doe@example.com', '+1-555-123-4567', '123 Main St, New York, NY', 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO users (first_name, last_name, email, phone, address, status, created_at, updated_at)
VALUES ('Jane', 'Smith', 'jane.smith@example.com', '+1-555-987-6543', '456 Oak Ave, Los Angeles, CA', 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO users (first_name, last_name, email, phone, address, status, created_at, updated_at)
VALUES ('Bob', 'Johnson', 'bob.johnson@example.com', '+1-555-456-7890', '789 Pine Rd, Chicago, IL', 'INACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO users (first_name, last_name, email, phone, address, status, created_at, updated_at)
VALUES ('Alice', 'Williams', 'alice.williams@example.com', '+1-555-321-0987', '321 Elm St, Houston, TX', 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO users (first_name, last_name, email, phone, address, status, created_at, updated_at)
VALUES ('Charlie', 'Brown', 'charlie.brown@example.com', '+1-555-654-3210', '654 Maple Dr, Phoenix, AZ', 'SUSPENDED', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
