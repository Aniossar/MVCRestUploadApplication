-- migration 1 date: 22.03.2023


-- rename old tables:
ALTER TABLE activity_table RENAME TO activity_table_old;
ALTER TABLE online_user_table RENAME TO online_user_table_old;
ALTER TABLE pricelist_table RENAME TO pricelist_table_old;
ALTER TABLE role_table RENAME TO role_table_old;
ALTER TABLE uploadedfiles RENAME TO uploadedfiles_old;
ALTER TABLE user_table RENAME TO user_table_old;
ALTER TABLE users_receipt_summary RENAME TO users_receipt_summary_old;

ALTER SEQUENCE activity_table_id_seq RENAME TO activity_table_old_id_seq;
ALTER SEQUENCE pricelist_table_id_seq RENAME TO pricelist_table_old_id_seq;
ALTER SEQUENCE role_table_id_seq RENAME TO role_table_old_id_seq;
ALTER SEQUENCE uploadedfiles_id_seq RENAME TO uploadedfiles_old_id_seq;
ALTER SEQUENCE user_table_id_seq RENAME TO user_table_old_id_seq;
ALTER SEQUENCE users_receipt_summary_id_seq RENAME TO users_receipt_summary_old_id_seq;


CREATE TABLE IF NOT EXISTS public.role_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL
    );

insert into role_table(name) values ('ROLE_ADMIN');
insert into role_table(name) values ('ROLE_MODERATOR');
insert into role_table(name) values ('ROLE_SUPPLIER');
insert into role_table(name) values ('ROLE_KEYMANAGER');
insert into role_table(name) values ('ROLE_SHOP');
insert into role_table(name) values ('ROLE_USER');


CREATE TABLE IF NOT EXISTS public.activity_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    activity_time timestamp with time zone NOT NULL,
    activity_type varchar(50) NOT NULL,
    user_id int NOT NULL,
    activity_message varchar(3000) NOT NULL
);


CREATE TABLE IF NOT EXISTS public.user_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login varchar(100) NOT NULL,
    password varchar(500) NOT NULL,
    email varchar(100),
    full_name varchar(100),
    company_name varchar(100),
    phone_number varchar(100),
    address varchar(500),
    certain_place_address varchar(500),
    appAccess varchar(50),
    role_id  integer,
    registration_time timestamp with time zone,
    enabled boolean
);

CREATE TABLE IF NOT EXISTS public.online_user_table
(
    user_id int NOT NULL PRIMARY KEY,
    last_ping_time timestamp with time zone NOT NULL
);


CREATE TABLE IF NOT EXISTS public.users_receipt_summary
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    activity_time timestamp with time zone NOT NULL,
    user_id int NOT NULL,
    company_name varchar(100),
    certain_place_address varchar(500),
    type varchar(200) NOT NULL,
    materials varchar(1000) NOT NULL,
    material_price double precision NOT NULL,
    add_price double precision NOT NULL,
    all_price double precision NOT NULL,
    main_coeff double precision NOT NULL,
    material_coeff double precision NOT NULL,
    slabs double precision NOT NULL,
    product_square double precision NOT NULL
);

CREATE TABLE IF NOT EXISTS public.shops_and_users_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    shop_id int NOT NULL,
    user_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS public.managers_users_table
(
    id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    key_manager_id int NOT NULL,
    user_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS public.user_refresh_table
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id int NOT NULL,
    refresh_token varchar(300) NOT NULL
    );



CREATE TABLE IF NOT EXISTS public.uploadedfiles
(

    id          int NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name        varchar(200),
    path        varchar(300),
    url         varchar(400),
    uploadDate  timestamp with time zone,
    size        bigint,
    hashcode    int,
    info        varchar(1000),
    for_clients varchar(500),
    author_id   int,
    PRIMARY KEY (id)
    );

CREATE TABLE  IF NOT EXISTS public.pricelist_table
(
    id         int NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name       varchar(200),
    path       varchar(300),
    url        varchar(400),
    uploadTime timestamp with time zone NOT NULL,
                             info        varchar(1000),
    for_clients varchar(500),
    author_id   int,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.statistics_table
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    time_slice timestamp with time zone NOT NULL,
    users_online int NOT NULL,
    new_receipts int NOT NULL,
    new_users int NOT NULL
);

-- insert data to activity_table1
insert into activity_table(id, activity_time, activity_type, activity_message, user_id)
    select activity_table_old.id,activity_table_old.activity_time,activity_table_old.activity_type,
        activity_table_old.activity_message, case WHEN user_table_old.id is NULL then 1 else user_table_old.id end
        from activity_table_old left join user_table_old ON activity_table_old.login = user_table_old.login;

-- insert data to user_table1
insert into user_table(id, login, password, email, full_name, company_name, phone_number, address,
    certain_place_address, appaccess, role_id, registration_time, enabled)
    select user_table_old.id,user_table_old.login,user_table_old.password,user_table_old.email,
        user_table_old.full_name,user_table_old.company_name,user_table_old.phone_number,user_table_old.address,
        user_table_old.certain_place_address,user_table_old.appaccess,user_table_old.role_id, null ,user_table_old.enabled
        from user_table_old;

-- insert data to online_user_table1
insert into online_user_table(user_id, last_ping_time)
    select user_id, last_ping_time from online_user_table_old;

-- insert data to users_receipt_summary1
insert into users_receipt_summary(id, activity_time, user_id, company_name, certain_place_address, type, materials,
    material_price, add_price, all_price, main_coeff, material_coeff, slabs, product_square )
    select id, activity_time, user_id, company_name, certain_place_address, type, materials,
        material_price, add_price, all_price, main_coeff, material_coeff, slabs, product_square
        from users_receipt_summary_old;

-- insert data to uploadedfiles1
insert into uploadedfiles(id, name, path, url, uploadDate, size, hashcode, info, for_clients, author_id)
select
    uploadedfiles_old.id,
    uploadedfiles_old.name,
    uploadedfiles_old.path,
    uploadedfiles_old.url,
    uploadedfiles_old.uploaddate,
    uploadedfiles_old.size,
    uploadedfiles_old.hashcode,
    uploadedfiles_old.info,
    uploadedfiles_old.for_clients,
    case WHEN user_table_old.id is NULL then 1 else user_table_old.id end
from uploadedfiles_old left join user_table_old ON uploadedfiles_old.author = user_table_old.login;

-- insert data to pricelist_table1
insert into pricelist_table(id, name, path, url, uploadtime, info, for_clients, author_id)
select
    pricelist_table_old.id,
    pricelist_table_old.name,
    pricelist_table_old.path,
    pricelist_table_old.url,
    pricelist_table_old.uploadtime,
    pricelist_table_old.info,
    pricelist_table_old.for_clients,
    case WHEN user_table_old.id is NULL then 1 else user_table_old.id end
from pricelist_table_old left join user_table_old ON pricelist_table_old.author = user_table_old.login;


select setval('activity_table_id_seq',(select last_value from activity_table_old_id_seq));
select setval('pricelist_table_id_seq',(select last_value from pricelist_table_old_id_seq));
select setval('role_table_id_seq',(select last_value from role_table_old_id_seq));
select setval('uploadedfiles_id_seq',(select last_value from uploadedfiles_old_id_seq));
select setval('user_table_id_seq',(select last_value from user_table_old_id_seq));
select setval('users_receipt_summary_id_seq',(select last_value from users_receipt_summary_old_id_seq));

-- rename new tables:
-- ALTER TABLE activity_table1 RENAME TO activity_table;
-- ALTER TABLE managers_users_table1 RENAME TO managers_users_table;
-- ALTER TABLE online_user_table1 RENAME TO online_user_table;
-- ALTER TABLE pricelist_table1 RENAME TO pricelist_table;
-- ALTER TABLE role_table1 RENAME TO role_table;
-- ALTER TABLE shops_and_users_table1 RENAME TO shops_and_users_table;
-- ALTER TABLE statistics_table1 RENAME TO statistics_table;
-- ALTER TABLE uploadedfiles1 RENAME TO uploadedfiles;
-- ALTER TABLE user_refresh_table1 RENAME TO user_refresh_table;
-- ALTER TABLE user_table1 RENAME TO user_table;
-- ALTER TABLE users_receipt_summary1 RENAME TO users_receipt_summary;