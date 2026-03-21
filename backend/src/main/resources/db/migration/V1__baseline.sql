CREATE TABLE IF NOT EXISTS integration_event (
    id CHAR(36) PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_key VARCHAR(120) NOT NULL,
    event_type VARCHAR(120) NOT NULL,
    payload_json JSON NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS process_checkpoint (
    id CHAR(36) PRIMARY KEY,
    business_key VARCHAR(120) NOT NULL,
    checkpoint_name VARCHAR(120) NOT NULL,
    checkpoint_status VARCHAR(60) NOT NULL,
    observed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_process_checkpoint UNIQUE (business_key, checkpoint_name, observed_at)
);
