package com.example.restaurant.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class Dish {

    private long idDish;

    private String title;

    private int weight;

    private String category;

    private int cost;

    private List<Ingredient> ingredients;

    private List<Cook> cooks;
}
