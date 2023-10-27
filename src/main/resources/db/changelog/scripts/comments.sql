-- liquibase formatted sql

-- changeset chikin:1
CREATE TABLE comments_table
(
    id           bigint NOT NULL,
    test_comment text,
    CONSTRAINT comments_table_pkey PRIMARY KEY (id)
);