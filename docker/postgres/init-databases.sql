-- Create databases for each microservice
CREATE DATABASE authdb;
CREATE DATABASE userdb;
CREATE DATABASE orderdb;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE authdb TO actora;
GRANT ALL PRIVILEGES ON DATABASE userdb TO actora;
GRANT ALL PRIVILEGES ON DATABASE orderdb TO actora;
