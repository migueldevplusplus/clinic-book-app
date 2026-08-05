CREATE TABLE doctor (
                          id           UUID PRIMARY KEY REFERENCES clinic_user(id),
                          specialty    VARCHAR(50) NOT NULL,
                          consultation_duration_minutes INT NOT NULL,
                          created_at   TIMESTAMP NOT NULL,
                          updated_at   TIMESTAMP NOT NULL
);