package com.example.restaurant.search;

import lombok.Data;

@Data
public class DishSearch {
    public String title;
    public String category;
    public Integer minCost;
    public Integer maxCost;
    public Integer order;

    public boolean isEmpty() {
        if(!title.isEmpty() || !category.isEmpty() ||
                minCost != null || maxCost != null ||
                order != null) {
            return false;
        }
        return true;
    }
}
