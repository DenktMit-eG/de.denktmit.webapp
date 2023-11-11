-- Define a sequence to generate unique values for User IDs.
-- The sequence starts with 10050 and increments by 50 for each new value.
-- It has no minimum or maximum value, and it uses a cache of 1.
CREATE SEQUENCE users_seq START WITH 10050 INCREMENT BY 50;
COMMENT ON SEQUENCE users_seq IS 'Sequence for Hibernate to derive user_id for users table';

-- Create a table to store user information.
-- Each user has a unique ID (user_id), email (mail), password, first name, last name,
-- an enabled status, and a role (either 'USER' or 'ADMIN').
CREATE TABLE users
(
    user_id                 bigint       NOT NULL PRIMARY KEY,      -- Define user_id as the primary key
    mail                    varchar(255) NOT NULL,                  -- User's email
    password                varchar(500) NOT NULL,                  -- Password (hashed or encrypted)
    disabled                BOOL CHECK (disabled IN (TRUE, FALSE)), -- User's enabled status
    locked_until            timestamptz  NOT NULL,                  -- Expiry date of user locking
    account_valid_until     timestamptz  NOT NULL,                  -- Expiry date of user account
    credentials_valid_until timestamptz  NOT NULL                   -- Expiry date of user credentials
);
CREATE UNIQUE INDEX uq_users_mail ON users (LOWER(mail));
COMMENT ON INDEX uq_users_mail IS 'Case insensitive unique index for User''s e-mail';

COMMENT ON COLUMN users.user_id IS 'Unique primary key for the user';
COMMENT ON COLUMN users.mail IS 'User''s unique email address';
COMMENT ON COLUMN users.password IS 'User''s hashed or encrypted password';
COMMENT ON COLUMN users.disabled IS 'User''s disabled status (true or false) based on administrative actions';
COMMENT ON COLUMN users.locked_until IS 'User''s locked status (true or false) based on temporary circumstances, e.g. failed logins';
COMMENT ON COLUMN users.account_valid_until IS 'User''s account expiry date, e.g. for paid time-limited access';
COMMENT ON COLUMN users.credentials_valid_until IS 'User''s credentials expiry date, e.g. to enforce password change after some time';

-- Define a sequence to generate unique values for group ids.
-- The sequence starts with 10100 and increments by 50 for each new value.
-- It has no minimum or maximum value, and it uses a cache of 1.
CREATE SEQUENCE groups_seq START WITH 10100 INCREMENT BY 50;
COMMENT ON SEQUENCE groups_seq IS 'Sequence for Hibernate to derive group_id for groups table';

CREATE TABLE groups
(
    group_id   bigint      NOT NULL PRIMARY KEY, -- Unique group ID
    group_name varchar(50) NOT NULL              -- Group's name
);

CREATE UNIQUE INDEX uq_groups_name ON groups (LOWER(group_name));
COMMENT ON INDEX uq_groups_name IS 'Case insensitive unique index for Group''s name';

COMMENT ON COLUMN groups.group_id IS 'Unique primary key for the Group';
COMMENT ON COLUMN groups.group_name IS 'Group''s unique name';


-- Define a sequence to generate unique values for OTP IDs.
-- The sequence starts with 10100 and increments by 50 for each new value.
-- It has no minimum or maximum value, and it uses a cache of 1.
CREATE SEQUENCE authorities_seq START WITH 10100 INCREMENT BY 50;
COMMENT ON SEQUENCE authorities_seq IS 'Sequence for Hibernate to derive group_id for groups table';

CREATE TABLE authorities
(
    authority_id bigint      NOT NULL PRIMARY KEY, -- Unique group ID
    authority    varchar(50) NOT NULL              -- Authority descriptor
);

CREATE UNIQUE INDEX uq_authorities_name ON authorities (LOWER(authority));
COMMENT ON INDEX uq_authorities_name IS 'Case insensitive unique index for Authority''s name';

COMMENT ON COLUMN authorities.authority_id IS 'Unique primary key for the authority';
COMMENT ON COLUMN authorities.authority IS 'Authority''s descriptive unique name';


-- Create a table to store group_authorities information.
CREATE TABLE group_authorities
(
    group_id     bigint NOT NULL, -- ID of the group
    authority_id bigint NOT NULL, -- ID of the authority

    -- Define a composite primary key
    PRIMARY KEY (group_id, authority_id)
);

-- Add comments to the columns in the 'group_authorities' table.
COMMENT ON COLUMN group_authorities.group_id IS 'ID of the group associated with this authority';
COMMENT ON COLUMN group_authorities.authority_id IS 'ID of the authority associated with this group';

-- Create foreign keys to enforce relationships with other tables.
ALTER TABLE group_authorities
    ADD CONSTRAINT fk_group_authority_group FOREIGN KEY (group_id) REFERENCES groups (group_id),
    ADD CONSTRAINT fk_group_authority_authority FOREIGN KEY (authority_id) REFERENCES authorities (authority_id);

-- Create a table to store group_members information.
CREATE TABLE group_members
(
    group_id bigint NOT NULL, -- ID of the group
    user_id  bigint NOT NULL, -- ID of the user

    -- Define a composite primary key
    PRIMARY KEY (group_id, user_id)
);

-- Add comments to the columns in the 'group_members' table.
COMMENT ON COLUMN group_members.group_id IS 'ID of the group to which the user belongs';
COMMENT ON COLUMN group_members.user_id IS 'ID of the user who is a member of the group';

-- Create foreign keys to enforce relationships with other tables.
ALTER TABLE group_members
    ADD CONSTRAINT fk_group_member_group FOREIGN KEY (group_id) REFERENCES groups (group_id),
    ADD CONSTRAINT fk_group_member_user FOREIGN KEY (user_id) REFERENCES users (user_id);


-- Create a table to store OTP (One-Time Password) information.
-- Each OTP has a unique ID (otp_id), an action, and an expiration timestamp (valid_until).
CREATE TABLE otp_actions
(
    otp_action_id uuid         NOT NULL, -- Unique OTP action ID
    target        varchar(255) NOT NULL, -- Description of the OTP action
    action        varchar(25)  NOT NULL, -- Description of the OTP action
    identifier    bigint       NOT NULL, -- Description of the OTP action
    valid_until   timestamptz  NOT NULL, -- Expiration timestamp
    PRIMARY KEY (otp_action_id)          -- Define otp_id as the primary key
);

-- Add comments to the columns in the 'otp' table.
COMMENT ON COLUMN otp_actions.otp_action_id IS 'Unique identifier for the OTP';
COMMENT ON COLUMN otp_actions.target IS 'The actions target descriptor, e.g. ''User''';
COMMENT ON COLUMN otp_actions.action IS 'The action descriptor, e.g. ''activate''';
COMMENT ON COLUMN otp_actions.identifier IS 'The actions target identifier, most likely a database id';
COMMENT ON COLUMN otp_actions.valid_until IS 'Timestamp when the OTP expires';


-- Fill default RBAC mappings
INSERT INTO groups (group_id, group_name)
VALUES (10001, 'administrators'), (10002, 'users');

INSERT INTO authorities (authority_id, authority)
VALUES (10001, 'ADMIN'), (10002, 'USER');

INSERT INTO group_authorities (group_id, authority_id)
VALUES (10001, 10001), (10001, 10002), (10002, 10001);
