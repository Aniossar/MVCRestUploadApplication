DROP TABLE IF EXISTS public.user_table;
DROP TABLE IF EXISTS public.role_table;
DROP TABLE IF EXISTS public.activity_table;
DROP TABLE IF EXISTS public.online_user_table;

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
    activity_message varchar(3000) NOT NULL
);

CREATE TABLE public.user_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login varchar(50) NOT NULL,
    password varchar(500) NOT NULL,
    email varchar(100),
    full_name varchar(100),
    company_name varchar(100),
    phone_number varchar(100),
    address varchar(500),
    certain_place_address varchar(500),
    appAccess varchar(50),
    role_id  integer
        constraint user_table_role_table_id_fk
            references role_table
);

create unique index user_table_login_uindex
    on user_table (login);

CREATE TABLE public.online_user_table
(
    user_login varchar(50) PRIMARY KEY NOT NULL REFERENCES user_table (login),
    last_ping_time timestamp with time zone NOT NULL
);