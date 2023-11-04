-- Insert sample OTP actions
INSERT INTO otp_actions (otp_action_id, action, valid_until)
VALUES
    ('95bdae47-a576-432b-b980-d66ced671eb1', 'Login', '2023-12-31 12:00:00'),
    ('3469a32f-dea6-4603-b4c9-4476f3975b25', 'Password Reset', '2023-11-30 18:30:00');

-- Insert sample users
INSERT INTO users (user_id, mail, password, first_name, last_name, enabled, role)
VALUES
    (-100, 'user1@example.com', 'hashed_password_1', 'John', 'Doe', TRUE, 'USER'),
    (-200, 'user2@example.com', 'hashed_password_2', 'Jane', 'Smith', TRUE, 'ADMIN');
