CREATE TABLE public.uploadedFiles (
  id int NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  name varchar(100),
  path varchar(300),
  url varchar(400),
  uploadDate date,
  size bigint,
  hashcode int,
  PRIMARY KEY (id)
) ;