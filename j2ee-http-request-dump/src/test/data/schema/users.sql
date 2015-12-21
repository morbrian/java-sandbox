CREATE TABLE users
(
  id bigint NOT NULL,
  username character varying,
  password character varying,
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT users_username_key UNIQUE (username)
)
