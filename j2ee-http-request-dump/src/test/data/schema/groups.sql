CREATE TABLE groups
(
  id bigint NOT NULL,
  groupname character varying,
  CONSTRAINT groups_pkey PRIMARY KEY (id),
  CONSTRAINT groups_groupname_key UNIQUE (groupname)
);
