CREATE TABLE IF NOT EXISTS public."user"
(
    id bigint NOT NULL DEFAULT nextval('user_id_seq'::regclass),
    full_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(16) COLLATE pg_catalog."default" NOT NULL,
    role character varying(10) COLLATE pg_catalog."default" NOT NULL,
    registered_at timestamp without time zone NOT NULL,
    is_subscribed boolean NOT NULL DEFAULT false,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT unique_fields UNIQUE (email)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public."user"
    OWNER to user1;

CREATE TABLE IF NOT EXISTS public.service
(
    id bigint NOT NULL DEFAULT nextval('service_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    description character varying(200) COLLATE pg_catalog."default",
    price numeric NOT NULL,
    active boolean NOT NULL DEFAULT true,
    CONSTRAINT service_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.service
    OWNER to user1;

CREATE TABLE IF NOT EXISTS public.barber
(
    id bigint NOT NULL DEFAULT nextval('barber_id_seq'::regclass),
    full_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default",
    phone character varying(50) COLLATE pg_catalog."default",
    rating numeric(5,0),
    CONSTRAINT barber_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.barber
    OWNER to user1;

CREATE TABLE IF NOT EXISTS public.booking
(
    id bigint NOT NULL DEFAULT nextval('booking_id_seq'::regclass),
    appointment_time timestamp without time zone NOT NULL,
    price_paid numeric(20,0) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    "status" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL DEFAULT nextval('booking_user_id_seq'::regclass),
    barber_id bigint NOT NULL DEFAULT nextval('booking_barber_id_seq'::regclass),
    service_id bigint NOT NULL DEFAULT nextval('booking_service_id_seq'::regclass),
    CONSTRAINT booking_pkey PRIMARY KEY (id),
    CONSTRAINT barber_id FOREIGN KEY (barber_id)
    REFERENCES public.barber (id) MATCH SIMPLE
                               ON UPDATE NO ACTION
                               ON DELETE NO ACTION,
    CONSTRAINT service_id FOREIGN KEY (service_id)
    REFERENCES public.service (id) MATCH SIMPLE
                               ON UPDATE NO ACTION
                               ON DELETE NO ACTION,
    CONSTRAINT user_id FOREIGN KEY (user_id)
    REFERENCES public."user" (id) MATCH SIMPLE
                               ON UPDATE NO ACTION
                               ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.booking
    OWNER to user1;