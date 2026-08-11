CREATE TABLE patient (
                          id           UUID PRIMARY KEY REFERENCES clinic_user(id),
                          birth_date   DATE      NOT NULL,
                          phone_number VARCHAR(50),
                          created_at   TIMESTAMP NOT NULL,
                          updated_at   TIMESTAMP NOT NULL
);