-- Insert sample users with custom account and credential expiration dates and locked status
INSERT INTO users (user_id, mail, password, disabled, locked_until, account_valid_until, credentials_valid_until)
VALUES
    (-100, 'user1.johndoe@example.com', '{noop}johndoe', FALSE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00'),
    (-200, 'admin2.janesmith@example.com', '{noop}janesmith', FALSE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00'),
    (-300, 'locked_user.alicejohnson@example.com', '{noop}alicejohnson', TRUE, '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00'),
    (-400, 'account_expired_user4.paulhiggins@example.com', '{noop}paulhiggins', TRUE, '1000-01-01 00:00:00.0+00', '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00'),
    (-500, 'creds_expired_user5.petergabriel@example.com', '{noop}petergabriel', TRUE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '1000-01-01 00:00:00.0+00');

-- Insert group memberships for authorization
INSERT INTO webapp.public.group_members (group_id, user_id)
VALUES (10002, -100), (10001, -200), (10002, -300), (10002, -400), (10002, -500);

-- Insert sample OTP actions
INSERT INTO otp_actions (otp_action_id, target, action, identifier, valid_until)
VALUES
    ('facade00-0000-4000-a000-000000000000', 'de.denktmit.webapp.persistence.users.User', 'activate', -100, '3000-01-01 00:00:00.0+00'),
    ('decade00-0000-4000-a000-000000000000', 'de.denktmit.webapp.persistence.users.User', 'password-reset', -200, '3000-01-01 00:00:00.0+00');
