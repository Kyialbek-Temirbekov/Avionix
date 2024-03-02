CREATE TABLE IF NOT EXISTS AUTHORITY (
    id VARCHAR(255) PRIMARY KEY,
    role VARCHAR(255),
    entity VARCHAR(255),
    create BOOLEAN,
    read BOOLEAN,
    update BOOLEAN,
    delete BOOLEAN
);


CREATE TABLE IF NOT EXISTS ACCOUNT (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(50),
    phone VARCHAR(25),
    password VARCHAR(100),
    enabled BOOLEAN,
    non_locked BOOLEAN,
    agreed_to_terms_of_use BOOLEAN,
    code VARCHAR(10),
    image BYTEA,
    created_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_at TIMESTAMP,
    updated_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS AIRLINE (
    base_id VARCHAR(36) PRIMARY KEY REFERENCES ACCOUNT(id) ON DELETE CASCADE,
    iata VARCHAR(3),
    name VARCHAR(50),
    address VARCHAR(50),
    official_website_url VARCHAR(50),
    description TEXT,
    client_id VARCHAR(50) UNIQUE,
    client_secret VARCHAR(100),
    priority SMALLINT
);

CREATE TABLE IF NOT EXISTS CUSTOMER (
    base_id VARCHAR(36) PRIMARY KEY REFERENCES ACCOUNT(id) ON DELETE CASCADE,
    first_name VARCHAR(25),
    last_name VARCHAR(25),
    gender VARCHAR(6),
    date_of_birth DATE,
    nationality VARCHAR(25),
    passport_id VARCHAR(25) UNIQUE,
    passport_expiry_date DATE
);

CREATE TABLE IF NOT EXISTS ACCOUNT_ROLES (
    account_id VARCHAR(36) REFERENCES ACCOUNT(id) ON DELETE CASCADE,
    roles VARCHAR(10)
);