CREATE TABLE clinic_user (
                       id          UUID PRIMARY KEY,
                       full_name   VARCHAR(255) NOT NULL,
                       email       VARCHAR(255) NOT NULL UNIQUE,
                       national_id  VARCHAR(50) NOT NULL UNIQUE,
                       password_hash    VARCHAR(255) NOT NULL,
                       role        VARCHAR(50)  NOT NULL,
                       created_at  TIMESTAMP    NOT NULL,
                       updated_at  TIMESTAMP    NOT NULL,
                       disabled_at TIMESTAMP
);

CREATE INDEX idx_user_email ON clinic_user(email);