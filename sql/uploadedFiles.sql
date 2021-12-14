CREATE TABLE public.uploadedFiles (
  id int NOT NULL,
  name varchar(100),
  path varchar(300),
  url varchar(400),
  uploadDate date,
  size bigint,
  PRIMARY KEY (id)
) ;