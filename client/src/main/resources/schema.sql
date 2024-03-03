CREATE TABLE IF NOT EXISTS AUTHORITY (
    id VARCHAR(50) PRIMARY KEY,
    role VARCHAR(10),
    entity VARCHAR(50),
    create BOOLEAN,
    read BOOLEAN,
    update BOOLEAN,
    delete BOOLEAN
);


CREATE TABLE IF NOT EXISTS ACCOUNT (
    id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(50),
    phone VARCHAR(50),
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
    base_id VARCHAR(50) PRIMARY KEY REFERENCES ACCOUNT(id) ON DELETE CASCADE,
    iata VARCHAR(10),
    name VARCHAR(50),
    address VARCHAR(50),
    official_website_url VARCHAR(50),
    description TEXT,
    client_id VARCHAR(50) UNIQUE,
    client_secret VARCHAR(100),
    priority SMALLINT
);

CREATE TABLE IF NOT EXISTS CUSTOMER (
    base_id VARCHAR(50) PRIMARY KEY REFERENCES ACCOUNT(id) ON DELETE CASCADE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    gender VARCHAR(10),
    date_of_birth DATE,
    nationality VARCHAR(50),
    passport_id VARCHAR(50),
    passport_expiry_date DATE
);

CREATE TABLE IF NOT EXISTS ACCOUNT_ROLES (
    account_id VARCHAR(50) REFERENCES ACCOUNT(id) ON DELETE CASCADE,
    roles VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS COMMENT (
    id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) REFERENCES CUSTOMER(base_id) ON DELETE CASCADE,
    description TEXT,
    created_at DATE,
    grade SMALLINT,
    checked BOOLEAN,
    lan VARCHAR(2)
);
