-- liquibase formatted sql

-- changeset laschishin:1 labels:main,test

create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    genre_id bigint references genres (id) on delete cascade,
    primary key (id)
);

create table book_authors (
    book_id bigint references books(id) on delete cascade,
    author_id bigint references authors(id) on delete cascade,
    primary key (book_id, author_id)
);