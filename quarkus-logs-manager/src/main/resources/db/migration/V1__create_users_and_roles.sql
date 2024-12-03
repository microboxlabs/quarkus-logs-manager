CREATE TABLE  logs_users (
                                   id int8 NOT NULL,
                                   "password" varchar(255) NULL,
                                   "role" varchar(255) NULL,
                                   username varchar(255) NULL,
                                   CONSTRAINT logs_users_pkey PRIMARY KEY (id)
);