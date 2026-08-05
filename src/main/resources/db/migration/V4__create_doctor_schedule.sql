CREATE TABLE doctor_schedule (
                          id           UUID PRIMARY KEY,
                          doctor_id    UUID REFERENCES doctor(id) NOT NULL,
                          day_of_week  VARCHAR(10) NOT NULL,
                          start_time   TIME NOT NULL,
                          end_time     TIME NOT NULL,
                          created_at   TIMESTAMP NOT NULL
);