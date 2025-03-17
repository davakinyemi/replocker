CREATE TABLE _user (
    id UUID PRIMARY KEY,
    hashed_email VARCHAR(64) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMPTZ NOT NULL,
    last_modified_date TIMESTAMPTZ
);