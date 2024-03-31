CREATE TABLE IF NOT EXISTS AIRPLANE (
    id VARCHAR(255) PRIMARY KEY,
    make VARCHAR(255),
    model VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS CLASS (
    id VARCHAR(255) PRIMARY KEY,
    airplane_id VARCHAR(255) REFERENCES AIRPLANE(id),
    cabin VARCHAR(255),
    seat_row SMALLINT,
    seat_col SMALLINT
);

CREATE TABLE CITY (
    code VARCHAR(255) PRIMARY KEY,
    country_code VARCHAR(255)
);

CREATE TABLE CITY_NAMES (
    id VARCHAR(36) PRIMARY KEY,
    city_code VARCHAR(255) REFERENCES CITY(code),
    name VARCHAR(255),
    lan VARCHAR(255)
);

CREATE TABLE FLIGHT (
    id VARCHAR(36) PRIMARY KEY,
    iata VARCHAR(255),
    airplane_id VARCHAR(255) REFERENCES AIRPLANE(id),
    one_way BOOLEAN,
    origin VARCHAR(255) REFERENCES CITY(code),
    destination VARCHAR(255) REFERENCES CITY(code),
    gate VARCHAR(255),
    currency VARCHAR(255),
    status VARCHAR(255),
    flight_duration INTEGER,
    transit_duration INTEGER,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE SEGMENT (
    id VARCHAR(36) PRIMARY KEY,
    flight_id VARCHAR(36) REFERENCES FLIGHT(id),
    departure_iata VARCHAR(255),
    departure_at TIMESTAMP,
    arrival_iata VARCHAR(255),
    arrival_at TIMESTAMP
);

CREATE TABLE TARIFF (
    id VARCHAR(36) PRIMARY KEY,
    flight_id VARCHAR(36) REFERENCES FLIGHT(id),
    cabin VARCHAR(255),
    price DOUBLE PRECISION,
    baggage_price DOUBLE PRECISION,
    discount SMALLINT,
    checked_baggage_included BOOLEAN,
    cabin_baggage_included BOOLEAN
);

CREATE TABLE IF NOT EXISTS TICKET (
    id VARCHAR(36) PRIMARY KEY,
    flight_id VARCHAR(36) REFERENCES FLIGHT(id),
    customer_id VARCHAR(255),
    seat VARCHAR(255),
    checked_baggage_included BOOLEAN,
    price DOUBLE PRECISION,
    status VARCHAR(255),
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);
