CREATE TABLE IF NOT EXISTS AIRLINE (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(50),
    phone VARCHAR(25),
    password VARCHAR(100),
    enabled BOOLEAN,
    non_locked BOOLEAN,
    agreed_to_terms_of_use BOOLEAN,
    code VARCHAR(10),
    IATA VARCHAR(3),
    name VARCHAR(50),
    logo BYTEA,
    address VARCHAR(50),
    official_website_url VARCHAR(50),
    description TEXT,
    client_id VARCHAR(50) UNIQUE,
    client_secret VARCHAR(100),
    priority INTEGER,
    created_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(50) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS CUSTOMER (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(50),
    phone VARCHAR(25),
    password VARCHAR(100),
    enabled BOOLEAN,
    non_locked BOOLEAN,
    agreed_to_terms_of_use BOOLEAN,
    code VARCHAR(10),
    first_name VARCHAR(25),
    last_name VARCHAR(25),
    gender VARCHAR(6),
    date_of_birth DATE,
    nationality VARCHAR(25),
    passport_id VARCHAR(25) UNIQUE,
    passport_expiry_date DATE,
    image BYTEA,
    created_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(50) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS AIRLINE_ROLES (
    airline_id VARCHAR(36) REFERENCES AIRLINE(id) ON DELETE CASCADE,
    roles VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS CUSTOMER_ROLES (
    customer_id VARCHAR(36) REFERENCES CUSTOMER(id) ON DELETE CASCADE,
    roles VARCHAR(10)
);