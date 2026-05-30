package com.booking.Booking_Project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookingApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Global Class Offering Booking System")
                        .version("1.0")
                        .description(
                                "Backend Engineering Assignment"));
    }
}