-- liquibase formatted sql

-- changeset laschishin:2 labels:main,test

create table book_comments (
    id bigserial,
    book_id bigint,
    text_content varchar(4000),
    primary key (id)
);
