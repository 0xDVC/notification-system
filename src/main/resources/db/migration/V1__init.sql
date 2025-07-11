CREATE TABLE notification_attempts (
    id BIGSERIAL PRIMARY KEY,
    attempt_number INTEGER NOT NULL,
    attempted_at TIMESTAMP,
    notification_id BIGINT,
    error_message VARCHAR(255),
    channel VARCHAR(16),
    status VARCHAR(16)
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    message VARCHAR(255),
    recipient VARCHAR(255),
    channel VARCHAR(16),
    status VARCHAR(16)
);

CREATE TABLE user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255),
    email_enabled BOOLEAN NOT NULL,
    sms_enabled BOOLEAN NOT NULL,
    push_enabled BOOLEAN NOT NULL,
    preferred_channel VARCHAR(16)
);

CREATE TABLE notification_templates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    subject VARCHAR(255),
    body VARCHAR(255),
    channel VARCHAR(16),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
); 