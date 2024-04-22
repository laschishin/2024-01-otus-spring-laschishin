-- liquibase formatted sql

-- changeset laschishin:2 labels:test

insert into book_comments(book_id, text_content)
values(1, 'BookComment_1');
values(1, 'BookComment_2');
