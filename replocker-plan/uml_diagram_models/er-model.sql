CREATE TYPE REPORT_TYPE AS ENUM ('CSV', 'XLSX');
CREATE TYPE ACCESS_REQUEST_TYPE AS ENUM ('PENDING', 'APPROVED', 'REJECTED');

CREATE TABLE admin (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(64) NOT NULL,
    keycloak_user_id UUID NOT NULL,
    created_date TIMESTAMPTZ NOT NULL ,
    last_modified_date TIMESTAMPTZ
);

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

CREATE TABLE report (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    size_bytes BIGINT NOT NULL,
    type REPORT_TYPE NOT NULL,
    report_collection_id UUID NOT NULL REFERENCES report_collection(id),
    created_date TIMESTAMP NOT NULL
);

CREATE TABLE access_request (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TEXT,
    status ACCESS_REQUEST_TYPE NOT NULL,
    report_collection_id UUID NOT NULL REFERENCES report_collection(id),
    admin_comment TEXT,
    created_date TIMESTAMPTZ NOT NULL
);

CREATE TABLE allowed_domain(
    id UUID PRIMARY KEY,
    domain_name VARCHAR(255) NOT NULL,
    admin_id UUID NOT NULL REFERENCES admin(id),
    created_date TIMESTAMPTZ NOT NULL,
    last_modified_date TIMESTAMPTZ
);

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

CREATE TABLE notification (
    id UUID PRIMARY KEY,
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    admin_id UUID NOT NULL REFERENCES admin(id),
    access_request_id UUID NOT NULL REFERENCES access_request(id),
    created_date TIMESTAMP NOT NULL
);

CREATE TABLE websocket_audit (
    id UUID PRIMARY KEY,
    admin_id UUID NOT NULL REFERENCES admin(id),
    connection_time TIMESTAMPTZ NOT NULL,
    disconnect_time TIMESTAMPTZ
);

CREATE UNIQUE INDEX uc_admin_username ON admin(username);
CREATE UNIQUE INDEX uc_admin_email ON admin(email);
CREATE UNIQUE INDEX uc_admin_keycloak_id ON admin(keycloak_user_id);

CREATE UNIQUE INDEX uc_domain_admin ON allowed_domain(admin_id, lower(domain_name));

CREATE UNIQUE INDEX uc_report_collection_name ON report_collection(name);
CREATE INDEX idx_collection_published ON report_collection(is_published, is_locked);

CREATE UNIQUE INDEX uc_report_name_report_collection ON report(name, report_collection_id);
CREATE INDEX idx_report_upload_date ON report(created_date);

CREATE UNIQUE INDEX uc_request_email_report_collection ON access_request(email, report_collection_id);

CREATE UNIQUE INDEX uc_token_value ON access_token(token_value);
CREATE INDEX idx_token_expiry ON access_token(expires_at);

CREATE INDEX idx_notification_admin_read ON notification(admin_id, is_read);

CREATE INDEX idx_websocket_admin ON websocket_audit(admin_id);