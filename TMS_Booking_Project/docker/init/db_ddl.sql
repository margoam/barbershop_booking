CREATE TABLE IF NOT EXISTS public."user"
(
    id bigserial PRIMARY KEY,
    full_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(60) COLLATE pg_catalog."default" NOT NULL,
    role character varying(10) COLLATE pg_catalog."default" NOT NULL,
    registered_at timestamp without time zone NOT NULL,
    is_subscribed boolean NOT NULL DEFAULT false,
    CONSTRAINT unique_fields UNIQUE (email)
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public."user"
    OWNER to user1;

CREATE TABLE IF NOT EXISTS public.service
(
    id bigserial PRIMARY KEY,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    description character varying(200) COLLATE pg_catalog."default",
    price numeric NOT NULL,
    active boolean NOT NULL DEFAULT true,
    duration integer NOT NULL DEFAULT 60
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.service
    OWNER to user1;

CREATE TABLE IF NOT EXISTS public.barber
(
    id bigserial PRIMARY KEY,
    full_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default",
    phone character varying(50) COLLATE pg_catalog."default",
    rating numeric(5,0)
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.barber
    OWNER to user1;

CREATE TABLE IF NOT EXISTS public.booking
(
    id bigserial PRIMARY KEY,
    appointment_time timestamp without time zone NOT NULL,
    price_paid numeric(20,0) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    status character varying(20) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL,
    barber_id bigint NOT NULL,
    service_id bigint NOT NULL,
    CONSTRAINT barber_id FOREIGN KEY (barber_id)
    REFERENCES public.barber (id) MATCH SIMPLE
                         ON UPDATE NO ACTION
                         ON DELETE CASCADE,
    CONSTRAINT service_id FOREIGN KEY (service_id)
    REFERENCES public.service (id) MATCH SIMPLE
                         ON UPDATE NO ACTION
                         ON DELETE CASCADE,
    CONSTRAINT user_id FOREIGN KEY (user_id)
    REFERENCES public."user" (id) MATCH SIMPLE
                         ON UPDATE NO ACTION
                         ON DELETE CASCADE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.booking
    OWNER to user1;

CREATE TABLE IF NOT EXISTS public.barber_schedule
(
    id bigserial PRIMARY KEY,
    barber_id bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    is_available boolean NOT NULL DEFAULT true,
    is_booked boolean NOT NULL DEFAULT false,
    CONSTRAINT barber_id FOREIGN KEY (barber_id)
    REFERENCES public.barber (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.barber_schedule
    OWNER to user1;
