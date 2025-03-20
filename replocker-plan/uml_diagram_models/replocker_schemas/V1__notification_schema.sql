CREATE TABLE notification (
    id UUID PRIMARY KEY,
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    admin_id UUID NOT NULL REFERENCES admin(id),
    access_request_id UUID NOT NULL REFERENCES access_request(id),
    created_date TIMESTAMP NOT NULL
);

CREATE INDEX idx_notification_admin_read ON notification(admin_id, is_read);