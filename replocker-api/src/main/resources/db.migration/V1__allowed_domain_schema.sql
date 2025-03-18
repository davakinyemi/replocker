CREATE TABLE allowed_domain(
    id UUID PRIMARY KEY,
    domain_name VARCHAR(255) NOT NULL,
    admin_id UUID NOT NULL REFERENCES admin(id),
    created_date TIMESTAMPTZ NOT NULL,
    last_modified_date TIMESTAMPTZ
);

CREATE UNIQUE INDEX idx_domain_admin ON allowed_domain(admin_id, lower(domain_name));