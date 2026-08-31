-- V6 seeded a super admin whose password hash is committed to this repository,
-- so anyone reading it holds the credentials of the most privileged account.
--
-- SuperAdminInitializer now creates that first administrator from ADMIN_EMAIL and
-- ADMIN_PASSWORD, but it only acts when no administrator exists. On a fresh
-- database V6 would run first and satisfy that check, leaving the account with
-- the public hash in place and the configured one never created. Migrations
-- finish before the initializer runs, so removing the row here closes that gap.
--
-- Matching on the leaked hash rather than on the address is deliberate: the
-- address is a real one that may well be reused for the configured administrator,
-- and that account must survive. Only the credential published in V6 is removed.
DELETE FROM clinic_user
WHERE role = 'SUPER_ADMIN'
  AND password_hash = '$2a$10$hbBsTVJ1QrR9nGHHQgxxD.AvkVJBRMaxCblle76bAN5JI8tQjmBtO';
