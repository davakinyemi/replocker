CREATE TABLE admin (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(64) NOT NULL,
    keycloak_user_id UUID NOT NULL,
    created_date TIMESTAMPTZ NOT NULL ,
    last_modified_date TIMESTAMPTZ
);

CREATE UNIQUE INDEX uc_admin_username ON admin(username);
CREATE UNIQUE INDEX uc_admin_email ON admin(email);
CREATE UNIQUE INDEX uc_admin_keycloak_id ON admin(keycloak_user_id);