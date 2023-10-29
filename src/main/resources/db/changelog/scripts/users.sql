-- liquibase formatted sql

-- changeset vKvs: Created_table_users
CREATE TABLE users(
    id serial NOT NULL,
    email character varying(32) NOT NULL CONSTRAINT max_min_email_lenght CHECK (char_length(email) >= 4 and char_length(email) <= 32),
    password character varying (16) NOT NULL CONSTRAINT max_min_password_lenght CHECK (char_length(password) >= 8 and char_length(password) <= 16),
    first_name character varying (16) CONSTRAINT max_min_first_name_lenght CHECK (char_length(first_name) >= 2 and char_length(first_name) <= 16),
    last_name character varying (16) CONSTRAINT max_min_last_name_lenght CHECK (char_length(last_name) >= 2 and char_length(last_name) <= 16),
    phone character varying (20),
    role varchar(255) NOT NULL,
    image text,
    CONSTRAINT user_pkey PRIMARY KEY (id)
);