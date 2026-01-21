-- Initial test users for Auth Service
-- Password for all users is: password123 (BCrypt encoded)
-- Note: user_id references the corresponding user in user-service

-- Admin user (links to user_id 1 in user-service)
INSERT INTO auth_users (user_id, username, email, password, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES (1, 'admin', 'admin@actora.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyelIeqRkSUO1gUaC3FzYSJ2fwvf/cSrp2', true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Manager user (links to user_id 2 in user-service)
INSERT INTO auth_users (user_id, username, email, password, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES (2, 'manager', 'manager@actora.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyelIeqRkSUO1gUaC3FzYSJ2fwvf/cSrp2', true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Regular user (links to user_id 3 in user-service)
INSERT INTO auth_users (user_id, username, email, password, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES (3, 'user', 'user@actora.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyelIeqRkSUO1gUaC3FzYSJ2fwvf/cSrp2', true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Assign roles
INSERT INTO user_roles (auth_user_id, role) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_roles (auth_user_id, role) VALUES (1, 'ROLE_MANAGER');
INSERT INTO user_roles (auth_user_id, role) VALUES (1, 'ROLE_USER');

INSERT INTO user_roles (auth_user_id, role) VALUES (2, 'ROLE_MANAGER');
INSERT INTO user_roles (auth_user_id, role) VALUES (2, 'ROLE_USER');

INSERT INTO user_roles (auth_user_id, role) VALUES (3, 'ROLE_USER');
