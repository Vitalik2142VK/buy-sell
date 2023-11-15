-- liquibase formatted sql

-- changeset morozov-1: created_table_announces
CREATE TABLE announces(
    id SERIAL NOT NULL,
    author_id INT REFERENCES users(id),
    description VARCHAR(64),
    image VARCHAR(32) NOT NULL,
    price INT NOT NULL,
    title VARCHAR(32),
    CONSTRAINT announce_pkey PRIMARY KEY (id)
);

-- changeset vKvs: changed_length_string_image
ALTER TABLE announces ALTER COLUMN image TYPE VARCHAR(128);
