-- Insert sample OTP actions
INSERT INTO otp_actions (otp_action_id, target, action, identifier, valid_until)
VALUES
    ('facade00-0000-4000-a000-000000000000', 'de.denktmit.webapp.persistence.users.User', 'activate', -100, '3000-01-01 00:00:00.0+00'),
    ('decade00-0000-4000-a000-000000000000', 'de.denktmit.webapp.persistence.users.User', 'password-reset', -200, '3000-01-01 00:00:00.0+00');

-- Insert sample users with custom account and credential expiration dates and locked status
INSERT INTO users (user_id, mail, password, disabled, locked_until, account_valid_until, credentials_valid_until, role)
VALUES
    (-100, 'user1@example.com', '{noop}hashed_password_1', FALSE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', 'USER'),
    (-200, 'admin2@example.com', '{noop}hashed_password_2', FALSE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', 'ADMIN'),
    (-300, 'locked_user3@example.com', '{noop}hashed_password_3', TRUE, '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', 'USER'),
    (-400, 'account_expired_user4@example.com', '{noop}hashed_password_4', TRUE, '1000-01-01 00:00:00.0+00', '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', 'USER'),
    (-500, 'credentials_expired_user5@example.com', '{noop}hashed_password_5', TRUE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '1000-01-01 00:00:00.0+00', 'USER');