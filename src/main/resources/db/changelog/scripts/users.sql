-- liquibase formatted sql

-- changeset vKvs: Created_table_users
CREATE TABLE users(
    id SERIAL NOT NULL,
    email CHARACTER VARYING(32) NOT NULL CONSTRAINT max_min_email_lenght CHECK (char_length(email) >= 4 and char_length(email) <= 32),
    first_name CHARACTER VARYING (16) CONSTRAINT max_min_first_name_lenght CHECK (char_length(first_name) >= 2 and char_length(first_name) <= 16),
    last_name CHARACTER VARYING (16) CONSTRAINT max_min_last_name_lenght CHECK (char_length(last_name) >= 2 and char_length(last_name) <= 16),
    phone CHARACTER VARYING (20),
    role VARCHAR(255) NOT NULL,
    image TEXT,
    CONSTRAINT user_pkey PRIMARY KEY (id)
);

-- changeset vKvs: Add_column_password_and_constraint_email
ALTER TABLE users ADD COLUMN password CHARACTER VARYING (255) NOT NULL;
ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE (email);