CREATE TABLE access_request (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TEXT,
    status VARCHAR(20) NOT NULL,
    report_collection_id UUID NOT NULL REFERENCES report_collection(id),
    admin_comment TEXT,
    created_date TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX uc_request_email_report_collection ON access_request(email, report_collection_id);