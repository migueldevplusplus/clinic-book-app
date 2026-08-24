-- Development seed: the system needs one SUPER_ADMIN to bootstrap doctors and
-- receptionists, since there is no public endpoint that creates one.
-- The password hash below is a local development credential.
-- Replace it before deploying this anywhere reachable.
INSERT INTO clinic_user VALUES (
                                   gen_random_uuid(),
                                   'Super Admin',
                                   'miguelmora32466@gmail.com',
                                   '$2a$10$hbBsTVJ1QrR9nGHHQgxxD.AvkVJBRMaxCblle76bAN5JI8tQjmBtO',
                                   'SUPER_ADMIN',
                                    NOW(),
                                    NOW()
                               );