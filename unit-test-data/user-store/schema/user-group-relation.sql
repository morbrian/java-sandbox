CREATE TABLE user_group_relation
(
  id serial primary key,
  user_id bigint,
  group_id bigint,
  CONSTRAINT user_group_relation_group_id_fkey FOREIGN KEY (group_id)
      REFERENCES groups (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_group_relation_user_id_fkey FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
