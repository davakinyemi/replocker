CREATE TABLE access_token (
    id UUID PRIMARY KEY,
    token_value VARCHAR(36) UNIQUE NOT NULL,
    collection_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE,

    -- Foreign Key Constraints
    CONSTRAINT fk_access_token_collection
        FOREIGN KEY (collection_id)
            REFERENCES report_collection(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_access_token_user
        FOREIGN KEY (user_id)
            REFERENCES _user(id)
            ON DELETE CASCADE
);

CREATE INDEX idx_token_expiry ON access_token(expires_at);