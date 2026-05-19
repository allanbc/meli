ALTER TABLE public.usuario
ADD CONSTRAINT uk_users_login UNIQUE (login);