package com.teachmeskills.tms_booking_project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "Barbershop booking service",
        description = "Diploma app for c32",
        contact = @Contact(
                name = "Rita Amosava",
                email = "mozheliukrita@gmail.com"
        )
))
@SpringBootApplication
public class TmsBookingProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmsBookingProjectApplication.class, args);
    }
}
