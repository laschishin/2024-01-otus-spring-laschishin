-- liquibase formatted sql

-- changeset laschishin:1 labels:main

insert into authors(full_name)
values ('Аркадий Стругацкий'), ('Борис Стругацкий'), ('Илья Ильф'), ('Евгений Петров');

insert into genres(name)
values ('Научная фантастика'), ('Приключения');

insert into books(title, genre_id)
values ('Понедельник начинается в субботу', 1), ('Жук в муравейнике', 1), ('Двенадцать стульев', 2), ('Поиск предназначения', 1);

insert into book_authors(book_id, author_id)
values (1, 1),   (1, 2),
       (2, 1),   (2, 2),
       (3, 3),   (3, 4),
       (4, 2);
