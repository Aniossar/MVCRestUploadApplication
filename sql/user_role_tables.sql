DROP TABLE public.user_table;
DROP TABLE public.role_table;
DROP TABLE public.activity_table;

CREATE TABLE public.role_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL
);

insert into role_table(name) values ('ROLE_ADMIN');
insert into role_table(name) values ('ROLE_MODERATOR');
insert into role_table(name) values ('ROLE_SUPPLIER');
insert into role_table(name) values ('ROLE_SHOP');
insert into role_table(name) values ('ROLE_USER');

CREATE TABLE public.activity_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    activity_time timestamp with time zone NOT NULL,
    activity_type varchar(50) NOT NULL,
    login varchar(100) NOT NULL,
    activity_message varchar(1000) NOT NULL
);

CREATE TABLE public.user_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login varchar(50) NOT NULL,
--         constraint user_table_activity_table_login_fk
--             references activity_table,
    password varchar(500) NOT NULL,
    email varchar(100) NOT NULL,
    role_id  integer
        constraint user_table_role_table_id_fk
            references role_table
);

create unique index user_table_login_uindex
    on user_table (login);