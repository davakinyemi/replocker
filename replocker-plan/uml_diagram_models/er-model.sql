CREATE TYPE report_type AS ENUM ('CSV', 'XLSX');
CREATE TYPE action_type AS ENUM ('VIEW', 'DOWNLOAD', 'REQUEST');

CREATE TABLE admin (
    admin_id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    keycloak_user_id UUID NOT NULL
);

CREATE TABLE "user" (
    user_id UUID PRIMARY KEY,
    hashed_email VARCHAR(64) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE report_collection (
    collection_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_locked BOOLEAN DEFAULT FALSE,
    is_published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    modified_at TIMESTAMPTZ DEFAULT NOW(),
    admin_id UUID REFERENCES admin(admin_id),
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMPTZ
);

CREATE TABLE report (
    report_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    size_bytes BIGINT NOT NULL,
    type report_type NOT NULL,
    upload_date TIMESTAMPTZ DEFAULT NOW(),
    collection_id UUID REFERENCES report_collection(collection_id)
);

CREATE TABLE access_token (
    token_id UUID PRIMARY KEY,
    token_value VARCHAR(36) UNIQUE NOT NULL,
    collection_id UUID REFERENCES report_collection(collection_id),
    user_id UUID REFERENCES "user"(user_id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    expires_at TIMESTAMPTZ NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    is_revoked BOOLEAN DEFAULT FALSE
);

CREATE TABLE allowed_domain (
    domain_id UUID PRIMARY KEY,
    domain_name VARCHAR(255) UNIQUE NOT NULL,
    admin_id UUID REFERENCES admin(admin_id)
);

CREATE TABLE activity_log (
    log_id UUID PRIMARY KEY,
    user_id UUID REFERENCES "user"(user_id),
    report_id UUID REFERENCES report(report_id),
    collection_id UUID REFERENCES report_collection(collection_id),
    action action_type NOT NULL,
    ip_address INET NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
