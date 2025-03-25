-- init.sql
CREATE DATABASE replocker;
CREATE DATABASE keycloak_replocker;

CREATE USER username WITH LOGIN PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE replocker, keycloak_replocker TO username;