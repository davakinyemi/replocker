CREATE TABLE websocket_audit (
    id UUID PRIMARY KEY,
    admin_id UUID NOT NULL REFERENCES admin(id),
    connection_time TIMESTAMPTZ NOT NULL,
    disconnect_time TIMESTAMPTZ
);

CREATE INDEX idx_websocket_admin ON websocket_audit(admin_id);