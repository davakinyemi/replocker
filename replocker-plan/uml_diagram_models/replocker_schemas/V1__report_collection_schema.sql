CREATE TABLE report_collection (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    is_published BOOLEAN NOT NULL DEFAULT FALSE,
    admin_id UUID NOT NULL REFERENCES admin(id),
    created_date TIMESTAMPTZ NOT NULL,
    last_modified_date TIMESTAMPTZ
);

CREATE UNIQUE INDEX uc_report_collection_name ON report_collection(name);
CREATE INDEX idx_collection_published ON report_collection(is_published, is_locked);