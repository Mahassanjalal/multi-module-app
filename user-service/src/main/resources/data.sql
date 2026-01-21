-- Initial test data for User Service
-- IDs 1, 2, 3 correspond to auth-service users (admin, manager, user)

-- Admin User (ID: 1)
INSERT INTO users (first_name, last_name, email, phone, address, status, created_at, updated_at)
VALUES ('Admin', 'User', 'admin@actora.com', '+1-555-000-0001', 'Admin Building, HQ', 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Manager User (ID: 2)
INSERT INTO users (first_name, last_name, email, phone, address, status, created_at, updated_at)
VALUES ('Manager', 'User', 'manager@actora.com', '+1-555-000-0002', 'Management Floor', 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Regular User (ID: 3)
INSERT INTO users (first_name, last_name, email, phone, address, status, created_at, updated_at)
VALUES ('Regular', 'User', 'user@actora.com', '+1-555-000-0003', '123 User Street', 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Additional test users
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
