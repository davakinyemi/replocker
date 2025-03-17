CREATE TABLE admin (
    id uuid PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    hashed_email VARCHAR(64) NOT NULL,
    keycloak_user_id uuid NOT NULL,
    created_date timestamptz NOT NULL ,
    last_modified_date timestamptz
);

CREATE UNIQUE INDEX idx_admin_username ON admin(username);
CREATE UNIQUE INDEX idx_admin_hashed_email ON admin(hashed_email);
CREATE UNIQUE INDEX idx_admin_keycloak_id ON admin(keycloak_user_id);