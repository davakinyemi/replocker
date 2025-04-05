CREATE TABLE access_token (
    id UUID PRIMARY KEY,
    token_value VARCHAR(36) UNIQUE NOT NULL,
    report_collection_id UUID NOT NULL REFERENCES report_collection(id),
    access_request_id UUID NOT NULL REFERENCES access_request(id),
    created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX uc_token_value ON access_token(token_value);
CREATE INDEX idx_token_expiry ON access_token(expires_at);