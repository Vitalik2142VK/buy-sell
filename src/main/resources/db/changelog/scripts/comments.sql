-- liquibase formatted sql

-- changeset chikin:Created_table_comment
CREATE TABLE comments(
    id SERIAL NOT NULL,
    text_comment CHARACTER VARYING(64) NOT NULL CONSTRAINT max_min_comment_length
        CHECK (char_length(text_comment) >= 8 and char_length(text_comment) <= 64),
    created_at BIGINT,
    author_id INT REFERENCES users(id),
    ad_id INT REFERENCES announces(id),
    CONSTRAINT comments_table_pkey PRIMARY KEY (id)
);