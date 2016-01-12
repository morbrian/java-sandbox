CREATE TABLE groups
(
  id        BIGINT NOT NULL,
  groupname character VARYING,
  CONSTRAINT groups_pkey PRIMARY KEY (id),
  CONSTRAINT groups_groupname_key UNIQUE (groupname)
);
