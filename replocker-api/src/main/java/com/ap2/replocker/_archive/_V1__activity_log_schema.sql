/*CREATE TABLE activity_log (
    id UUID PRIMARY KEY,
    activity_type VARCHAR(20) NOT NULL CHECK (activity_type IN ('ACCESS', 'DOWNLOAD', 'REQUEST')),
    ip_address INET NOT NULL,
    user_id UUID REFERENCES _user(id),
    report_id UUID REFERENCES report(id),
    collection_id UUID NOT NULL REFERENCES report_collection(id),
    created_date TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_activity_log_created ON activity_log(created_date);
CREATE INDEX idx_activity_log_collection ON activity_log(collection_id);*/