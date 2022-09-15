package com.example.restaurant.model.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Cook {
    private long idCook;
    private String name;
    private String surname;
    private Date date;
}
