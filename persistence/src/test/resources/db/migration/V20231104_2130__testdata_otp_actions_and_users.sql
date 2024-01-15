-- Insert sample users with custom account and credential expiration dates and locked status
INSERT INTO users (user_id, mail, mail_verified, password, disabled, locked_until, account_valid_until, credentials_valid_until)
VALUES
    (-100, 'user1.johndoe@example.com', TRUE, '{noop}johndoe', FALSE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00'),
    (-200, 'admin2.janesmith@example.com', TRUE, '{noop}janesmith', FALSE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00'),
    (-300, 'locked_user.alicejohnson@example.com', TRUE, '{noop}alicejohnson', TRUE, '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00'),
    (-400, 'account_expired_user4.paulhiggins@example.com', FALSE, '{noop}paulhiggins', TRUE, '1000-01-01 00:00:00.0+00', '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00'),
    (-500, 'creds_expired_user5.petergabriel@example.com', FALSE, '{noop}petergabriel', FALSE, '1000-01-01 00:00:00.0+00', '3000-01-01 00:00:00.0+00', '1000-01-01 00:00:00.0+00');

-- Insert group memberships for authorization
INSERT INTO group_members (group_id, user_id)
VALUES (10002, -100), (10001, -200), (10002, -300), (10002, -400), (10002, -500);

-- Insert sample OTP actions
INSERT INTO otp_actions (action_id, token, user_id, action, valid_until)
VALUES
    (-100, 'facade00-0000-4000-a000-000000000000', -100, 'verify-email', '3000-01-01 00:00:00.0+00'),
    (-200, 'decade00-0000-4000-a000-000000000000', -200, 'reset-password', '3000-01-01 00:00:00.0+00'),
    (-300, 'ad0be000-0000-4000-a000-000000000000', -500, 'accept-invite', '3000-01-01 00:00:00.0+00');
