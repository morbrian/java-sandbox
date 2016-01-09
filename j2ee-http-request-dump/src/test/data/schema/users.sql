CREATE TABLE users
(
  id       BIGINT NOT NULL,
  username CHARACTER VARYING,
  password CHARACTER VARYING,
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT users_username_key UNIQUE (username)
)
