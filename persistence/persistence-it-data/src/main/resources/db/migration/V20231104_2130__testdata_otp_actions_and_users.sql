-- Insert sample OTP actions
INSERT INTO otp_actions (otp_action_id, object, action, identifier, valid_until)
VALUES
    ('facade00-0000-4000-a000-000000000000', 'User', 'Activate', -100, '9999-12-31 23:59:59.999999+00'),
    ('decade00-0000-4000-a000-000000000000', 'User', 'Password Reset', -200, '9999-12-31 23:59:59.999999+00');

-- Insert sample users with custom account and credential expiration dates and locked status
INSERT INTO users (user_id, mail, password, first_name, last_name, disabled, locked_until, account_valid_until, credentials_valid_until, role)
VALUES
    (-100, 'user1@example.com', 'hashed_password_1', 'John', 'Doe', FALSE, NULL, '9999-12-31 23:59:59.999999+00', '9999-12-31 23:59:59.999999+00', 'USER'),
    (-200, 'admin2@example.com', 'hashed_password_2', 'Jane', 'Smith', FALSE, NULL, '9999-12-31 23:59:59.999999+00', '9999-12-31 23:59:59.999999+00', 'ADMIN'),
    (-300, 'locked_user3@example.com', 'hashed_password_3', 'Alice', 'Johnson', TRUE, '1970-01-01 00:00:00+00', '9999-12-31 23:59:59.999999+00', '9999-12-31 23:59:59.999999+00', 'USER'),
    (-400, 'account_expired_user4@example.com', 'hashed_password_3', 'Paul', 'Higgins', TRUE, NULL, '1970-01-01 00:00:00+00', '9999-12-31 23:59:59.999999+00', 'USER'),
    (-500, 'credentials_expired_user5@example.com', 'hashed_password_3', 'Peter', 'Gabriel', TRUE, NULL, '9999-12-31 23:59:59.999999+00', '1970-01-01 00:00:00+00', 'USER');
