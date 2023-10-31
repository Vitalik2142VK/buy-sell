-- liquibase formatted sql

-- changeset morozov-1: created_table_announces
CREATE TABLE announces(
    id BIGSERIAL PRIMARY KEY NOT NULL CONSTRAINT id_not_null,
    pk INT NOT NULL CONSTRAINT pk_not_null,
    author_first_name VARCHAR(32),
    author_last_name VARCHAR(32),
    description VARCHAR(64),
    email VARCHAR(32),
    image VARCHAR(32) NOT NULL CONSTRAINT image_not_null,
    phone VARCHAR(32),
    price INT,
    title VARCHAR(32)
);

