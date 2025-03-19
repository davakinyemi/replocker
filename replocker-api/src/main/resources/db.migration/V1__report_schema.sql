CREATE TABLE report (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    size_bytes BIGINT NOT NULL,
    type VARCHAR(4) NOT NULL CHECK (type IN ('CSV', 'XLSX')),
    report_collection_id UUID NOT NULL REFERENCES report_collection(id),
    created_date TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX uc_report_name_report_collection ON report(name, report_collection_id);
CREATE INDEX idx_report_upload_date ON report(created_date);