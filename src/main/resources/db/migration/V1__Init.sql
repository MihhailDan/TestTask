CREATE SEQUENCE  IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE proxy (
  id BIGINT NOT NULL,
   name VARCHAR(120),
   type VARCHAR(255),
   hostname VARCHAR(120),
   port INTEGER,
   username VARCHAR(30),
   password VARCHAR(30),
   active BOOLEAN,
   CONSTRAINT pk_proxy PRIMARY KEY (id)
);


ALTER TABLE proxy ADD CONSTRAINT uc_proxy_hostname UNIQUE (hostname);
ALTER TABLE proxy ADD CONSTRAINT uc_proxy_name UNIQUE (name);