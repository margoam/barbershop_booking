-- 1. Create 3 barbers
INSERT INTO public.barber (full_name, email, phone, rating)
VALUES
    ('John Smith', 'john.smith@barbershop.com', '+1234567890', 5),
    ('Michael Brown', 'michael.brown@barbershop.com', '+1987654321', 4),
    ('David Wilson', 'david.wilson@barbershop.com', '+1122334455', 5);

-- 2. Create admin user, password = 1234567891011
INSERT INTO public."user" (full_name, email, password, role, registered_at, is_subscribed)
VALUES
    ('Admin User', 'test@test.com', '$2a$10$NTGpqciaNf.1j/xquiWv4eN7zGjEY8E7vWNkiAEit9KXgyQ76aiT.', 'ADMIN', NOW(), true);

-- 3. Create services
INSERT INTO public.service (name, description, price, active, duration)
VALUES
    ('Haircut', 'Standard haircut with scissors and clippers', 30.00, true, 45),
    ('Haircut + Beard', 'Haircut with full beard trim', 45.00, true, 60),
    ('Beard + Buzz Cut', 'Full beard service with buzz cut', 50.00, true, 75),
    ('Beard Trim', 'Beard shaping and trimming', 25.00, true, 30),
    ('Beard Coloring', 'Beard trim with coloring service', 40.00, true, 60),
    ('Full Combo', 'Haircut, beard service with coloring', 65.00, true, 90);

-- 4. Create schedules for barbers (May and June, excluding Sundays/Mondays, with lunch breaks)
DO $$
    DECLARE
        barber_record RECORD;
        current_day DATE;
        start_time TIMESTAMP;
        end_time TIMESTAMP;
        lunch_start TIMESTAMP;
        lunch_end TIMESTAMP;
    BEGIN
        FOR barber_record IN SELECT id FROM public.barber LOOP
                FOR current_day IN
                    SELECT generate_series(
                                   '2025-05-01'::DATE,
                                   '2025-06-30'::DATE,
                                   '1 day'::INTERVAL
                           )::DATE
                    LOOP
                        -- Skip Sundays (0) and Mondays (1) - days off
                        IF EXTRACT(DOW FROM current_day) NOT IN (0, 1) THEN
                            -- Morning session (10:00-13:00)
                            start_time := (current_day + TIME '10:00:00')::TIMESTAMP;
                            end_time := (current_day + TIME '13:00:00')::TIMESTAMP;

                            INSERT INTO public.barber_schedule
                            (barber_id, start_time, end_time, is_available, is_booked)
                            VALUES (barber_record.id, start_time, end_time, true, false);

                            -- Lunch break (13:00-14:00) - not available
                            lunch_start := (current_day + TIME '13:00:00')::TIMESTAMP;
                            lunch_end := (current_day + TIME '14:00:00')::TIMESTAMP;

                            INSERT INTO public.barber_schedule
                            (barber_id, start_time, end_time, is_available, is_booked)
                            VALUES (barber_record.id, lunch_start, lunch_end, false, false);

                            -- Afternoon session (14:00-20:00)
                            start_time := (current_day + TIME '14:00:00')::TIMESTAMP;
                            end_time := (current_day + TIME '20:00:00')::TIMESTAMP;

                            INSERT INTO public.barber_schedule
                            (barber_id, start_time, end_time, is_available, is_booked)
                            VALUES (barber_record.id, start_time, end_time, true, false);
                        END IF;
                    END LOOP;
            END LOOP;
    END $$;

-- Create a test customer user, password = 1234567891011
INSERT INTO public."user" (full_name, email, password, role, registered_at, is_subscribed)
VALUES
    ('Test Customer', 'customer@test.com', '$2a$10$NTGpqciaNf.1j/xquiWv4eN7zGjEY8E7vWNkiAEit9KXgyQ76aiT.', 'USER', NOW(), false);