truncate table meals, user_roles, users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO MEALS (DATE_TIME, DESCRIPTION, CALORIES, USER_ID)
VALUES ('2015-06-1 14:0'::timestamp, 'Админ ланч', 510, 100001),
       ('2015-06-1 21:0'::timestamp, 'Админ ужин', 1500, 100001),
       ('2020-01-30 10:0'::timestamp, 'Завтрак', 500, 100000),
       ('2020-01-30 13:0'::timestamp, 'Обед', 1000, 100000),
       ('2020-01-30 20:0'::timestamp, 'Ужин', 500, 100000),
       ('2020-01-31 0:0'::timestamp, 'Еда на граничное значение', 100, 100000),
       ('2020-01-31 10:0'::timestamp, 'Завтрак', 1000, 100000),
       ('2020-01-31 13:0'::timestamp, 'Обед', 500, 100000),
       ('2020-01-31 20:0'::timestamp, 'Ужин', 410, 100000);