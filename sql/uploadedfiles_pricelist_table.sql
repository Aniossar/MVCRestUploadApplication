DROP TABLE IF EXISTS public.pricelist_table;
DROP TABLE IF EXISTS public.uploadedFiles;

CREATE TABLE public.uploadedFiles
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
    author      varchar(200),
    PRIMARY KEY (id)
);

CREATE TABLE public.pricelist_table
(
    id         int NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name       varchar(200),
    path       varchar(300),
    url        varchar(400),
    uploadTime timestamp with time zone NOT NULL,
    info        varchar(1000),
    for_clients varchar(500),
    author     varchar(200),
    PRIMARY KEY (id)
);