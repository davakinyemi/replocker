CREATE TABLE admin (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    hashed_email VARCHAR(64) NOT NULL,
    keycloak_user_id UUID NOT NULL,
    created_date TIMESTAMPTZ NOT NULL ,
    last_modified_date TIMESTAMPTZ
);

CREATE UNIQUE INDEX uc_admin_username ON admin(username);
CREATE UNIQUE INDEX uc_admin_hashed_email ON admin(hashed_email);
CREATE UNIQUE INDEX uc_admin_keycloak_id ON admin(keycloak_user_id);