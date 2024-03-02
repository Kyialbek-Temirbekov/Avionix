CREATE TABLE FAQ (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);
CREATE TABLE FAQ_CONTENT (
    id VARCHAR(36) PRIMARY KEY,
    faq_id VARCHAR(36) REFERENCES FAQ(id) ON DELETE CASCADE,
    question VARCHAR(500),
    answer TEXT,
    lan VARCHAR(2)
);
CREATE TABLE SKYLINE_BENEFITS (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_at TIMESTAMP,
    updated_by VARCHAR(50),
    logo BYTEA
);
CREATE TABLE SKYLINE_BENEFITS_CONTENT (
    id VARCHAR(36) PRIMARY KEY,
    skyline_benefits_id VARCHAR(36) REFERENCES SKYLINE_BENEFITS(id) ON DELETE CASCADE,
    title VARCHAR(500),
    description TEXT,
    lan VARCHAR(2)
);
CREATE TABLE PRIVACY_POLICY (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_at TIMESTAMP,
    updated_by VARCHAR(50)
);
CREATE TABLE PRIVACY_POLICY_CONTENT (
    id VARCHAR(36) PRIMARY KEY,
    privacy_policy_id VARCHAR(36) REFERENCES PRIVACY_POLICY(id) ON DELETE CASCADE,
    title VARCHAR(500),
    description TEXT,
    lan VARCHAR(2)
);
CREATE TABLE TERMS_OF_USE (
    id VARCHAR(36) PRIMARY KEY,
    type VARCHAR(10),
    created_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_at TIMESTAMP,
    updated_by VARCHAR(50)
);
CREATE TABLE TERMS_OF_USE_CONTENT (
    id VARCHAR(36) PRIMARY KEY,
    terms_of_use_id VARCHAR(36) REFERENCES TERMS_OF_USE(id) ON DELETE CASCADE,
    title VARCHAR(500),
    description TEXT,
    lan VARCHAR(2)
);
CREATE TABLE CONTACT (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    message TEXT,
    created_at DATE
);

