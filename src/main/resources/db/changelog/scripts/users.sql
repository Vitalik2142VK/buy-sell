-- liquibase formatted sql

-- changeset vKvs:1
CREATE TABLE test_table1 (
    id bigint NOT NULL,
    test_text text,
    CONSTRAINT test_table1_pkey PRIMARY KEY (id)
);