-- liquibase formatted sql

-- changeset chikin:created_table_comment
CREATE TABLE comments
(
    id bigint NOT NULL,
    test_comment text,
    CONSTRAINT comments_table_pkey PRIMARY KEY (id)
);