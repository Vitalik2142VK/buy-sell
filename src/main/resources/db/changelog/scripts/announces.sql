-- liquibase formatted sql

-- changeset morozov-1: created_table_announces
CREATE TABLE announces(
    id SERIAL NOT NULL,
    pk INT NOT NULL,
    author_first_name VARCHAR(32),
    author_last_name VARCHAR(32),
    description VARCHAR(64),
    email VARCHAR(32) NOT NULL,
    image VARCHAR(32) NOT NULL,
    phone VARCHAR(32),
    price INT,
    title VARCHAR(32),
    CONSTRAINT announce_pkey PRIMARY KEY (id)
);

