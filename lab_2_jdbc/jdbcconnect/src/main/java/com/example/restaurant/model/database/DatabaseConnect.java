package com.example.restaurant.model.database;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConnect {
    @Bean
    @Scope("singleton")
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost/restaurant", "postgres", "1961");
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }
}
