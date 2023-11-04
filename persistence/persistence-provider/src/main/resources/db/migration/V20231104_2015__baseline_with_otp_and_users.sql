-- Define a sequence to generate unique values for OTP IDs.
-- The sequence starts with 10050 and increments by 50 for each new value.
-- It has no minimum or maximum value, and it uses a cache of 1.
CREATE SEQUENCE hibernate_sequence
    START WITH 10050
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Create a table to store OTP (One-Time Password) information.
-- Each OTP has a unique ID (otp_id), an action, and an expiration timestamp (valid_until).
CREATE TABLE otp_actions
(
    otp_action_id uuid        NOT NULL, -- Unique OTP action ID
    action        varchar(25) NOT NULL, -- Description of the OTP action
    valid_until   timestamptz NOT NULL, -- Expiration timestamp
    PRIMARY KEY (otp_action_id)         -- Define otp_id as the primary key
);

-- Add comments to the columns in the 'otp' table.
COMMENT ON COLUMN otp_actions.otp_action_id IS 'Unique identifier for the OTP';
COMMENT ON COLUMN otp_actions.action IS 'Description of the OTP action';
COMMENT ON COLUMN otp_actions.valid_until IS 'Timestamp when the OTP expires';

-- Create a table to store user information.
-- Each user has a unique ID (user_id), email (mail), password, first name, last name,
-- an enabled status, and a role (either 'USER' or 'ADMIN').
CREATE TABLE users
(
    user_id    bigint       NOT NULL,                                   -- Unique user ID
    mail       varchar(255) NOT NULL,                                   -- User's email
    password   char(60)     NOT NULL,                                   -- Password (hashed or encrypted)
    first_name varchar(255) NOT NULL,                                   -- User's first name
    last_name  varchar(255) NOT NULL,                                   -- User's last name
    enabled    BOOL CHECK (enabled IN (TRUE, FALSE)),                   -- User's enabled status
    role       varchar(15)  NOT NULL CHECK (role IN ('USER', 'ADMIN')), -- User's role
    PRIMARY KEY (user_id),                                              -- Define user_id as the primary key
    UNIQUE (mail)                                                       -- Ensure email uniqueness
);

-- Add comments to the columns in the 'users' table.
COMMENT ON COLUMN users.user_id IS 'Unique identifier for the user';
COMMENT ON COLUMN users.mail IS 'User''s email address';
COMMENT ON COLUMN users.password IS 'User''s hashed or encrypted password';
COMMENT ON COLUMN users.first_name IS 'User''s first name';
COMMENT ON COLUMN users.last_name IS 'User''s last name';
COMMENT ON COLUMN users.enabled IS 'User''s enabled status (true or false)';
COMMENT ON COLUMN users.role IS 'User''s role, can be ''USER'' or ''ADMIN''';
