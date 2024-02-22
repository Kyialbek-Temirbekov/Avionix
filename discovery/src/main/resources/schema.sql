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
