CREATE TABLE appointment (
                             id                 UUID PRIMARY KEY,
                             patient_id         UUID REFERENCES patient(id) NOT NULL,
                             doctor_id          UUID REFERENCES doctor(id) NOT NULL,
                             date               DATE NOT NULL,
                             start_time         TIME NOT NULL,
                             end_time           TIME NOT NULL,
                             appointment_status VARCHAR(20) NOT NULL,
                             created_at         TIMESTAMP NOT NULL,
                             updated_at         TIMESTAMP NOT NULL,
                             disabled_at        TIMESTAMP
);