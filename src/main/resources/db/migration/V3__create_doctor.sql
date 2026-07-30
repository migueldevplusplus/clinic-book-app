CREATE TABLE patient (
                          id           UUID PRIMARY KEY,
                          birth_date   DATE      NOT NULL,
                          phone_number VARCHAR(50),
                          user_id UUID REFERENCES clinic_user(id) UNIQUE,
                          created_at   TIMESTAMP NOT NULL,
                          updated_at   TIMESTAMP NOT NULL
);