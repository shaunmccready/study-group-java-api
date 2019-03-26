DO
  $$
    BEGIN
      CREATE TABLE IF NOT EXISTS public.group
      (
        id       BIGSERIAL NOT NULL,
        name     text      NOT NULL,
        owner_id TEXT      NOT NULL,
        created  timestamp without time zone DEFAULT now(),
        modified timestamp without time zone DEFAULT now(),
        CONSTRAINT group_id_pkey PRIMARY KEY (id),
        CONSTRAINT fk_group_owner_id FOREIGN KEY (owner_id)
          REFERENCES public."user" (id) MATCH SIMPLE
          ON UPDATE CASCADE ON DELETE CASCADE
      )
        WITH (
          OIDS = FALSE
        )
        TABLESPACE pg_default;

      IF NOT EXISTS(SELECT *
                    FROM information_schema.column_privileges
                    where grantee = 'studygroup-admin'
                      and table_name = 'group'
                    order by grantee desc, privilege_type) THEN

        GRANT ALL ON TABLE public.group TO "studygroup-admin";

      END IF;

      IF NOT EXISTS(SELECT *
                    FROM information_schema.column_privileges
                    where grantee = 'studygroup_rw'
                      and table_name = 'group'
                    order by grantee desc, privilege_type) THEN
        GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.group TO studygroup_rw;

      END IF;


    END;
    $$;



DO
  $$
    BEGIN
      CREATE TABLE IF NOT EXISTS public.user_group
      (
        id        BIGSERIAL,
        group_id  bigint  NOT NULL,
        member_id TEXT    NOT NULL,
        approved  BOOLEAN NOT NULL,
        created   timestamp without time zone DEFAULT now(),
        modified  timestamp without time zone DEFAULT now(),
        CONSTRAINT user_group_id_pkey PRIMARY KEY (id),
        CONSTRAINT fk_user_group_member_id FOREIGN KEY (member_id)
          REFERENCES public."user" (id) MATCH SIMPLE
          ON UPDATE CASCADE ON DELETE CASCADE
      )
        WITH (
          OIDS = FALSE
        )
        TABLESPACE pg_default;

      IF NOT EXISTS(SELECT *
                    FROM information_schema.column_privileges
                    where grantee = 'studygroup-admin'
                      and table_name = 'user_group'
                    order by grantee desc, privilege_type) THEN

        GRANT ALL ON TABLE public.user_group TO "studygroup-admin";

      END IF;

      IF NOT EXISTS(SELECT *
                    FROM information_schema.column_privileges
                    where grantee = 'studygroup_rw'
                      and table_name = 'user_group'
                    order by grantee desc, privilege_type) THEN
        GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.user_group TO studygroup_rw;

      END IF;


    END;
    $$