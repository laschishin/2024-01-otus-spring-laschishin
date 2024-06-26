-- liquibase formatted sql

-- changeset laschishin:1 labels:test

insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3'),
       ('Author_4'), ('Author_5'), ('Author_6');;

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, genre_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into book_authors(book_id, author_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);
