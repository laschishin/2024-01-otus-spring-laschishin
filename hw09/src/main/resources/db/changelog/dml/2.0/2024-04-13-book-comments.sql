-- liquibase formatted sql

-- changeset laschishin:2 labels:main,test

create table book_comments (
    id bigserial,
    book_id bigint,
    text_content varchar(4000),
    primary key (id)
);

create index book_comments_n1 on book_comments(book_id);

alter table book_comments
    add constraint book_comments_fk1
    foreign key (book_id)
    references books
    on delete cascade;