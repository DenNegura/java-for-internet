package com.example.restaurant.model.DAO;

import com.example.restaurant.model.entity.Cook;
import com.example.restaurant.model.entity.Dish;
import com.example.restaurant.model.entity.Ingredient;
import com.example.restaurant.search.DishSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DishDAO implements DAO<Dish, Long> {

    @Autowired
    Connection connection;

    @Autowired
    IngredientDAO ingredientDAO;

    @Autowired
    CookDAO cookDAO;
    @Override
    public List<Dish> getAll() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select " +
                    "id_dish, title, weight, category, cost " +
                    "from dish;");
            List<Dish> list = new ArrayList<>();
            while (resultSet.next()) {
                Dish dish = new Dish();
                dish.setIdDish(resultSet.getLong("id_dish"));
                dish.setTitle(resultSet.getString("title"));
                dish.setWeight(resultSet.getInt("weight"));
                dish.setCategory(resultSet.getString("category"));
                dish.setCost(resultSet.getInt("cost"));
                getIngredients(dish);
                getCooks(dish);
                list.add(dish);
            }
            return list;
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    public List<Dish> getSearch(DishSearch dishSearch) {
        if(dishSearch == null || dishSearch.isEmpty()) {
            return getAll();
        }
        try {
            String search = "select id_dish, title, weight, category, cost from dish where ";
            if(!dishSearch.title.isEmpty()) {
                search += "title = '" + dishSearch.title + "' ";
                if(!dishSearch.category.isEmpty()) {
                    search+= "and ";
                }
            }
            if(!dishSearch.category.isEmpty()) {
                search += "category = '" + dishSearch.category + "' ";
                if(dishSearch.minCost != null || dishSearch.maxCost != null) {
                    search += "and ";
                }
            }
            if(dishSearch.minCost != null && dishSearch.maxCost != null) {
                search += "cost between " + dishSearch.minCost + " and " + dishSearch.maxCost + " ";
                if(dishSearch.order != null) {
                    search += "and ";
                }
            }
            else if(dishSearch.minCost != null) {
                search += "cost > " + dishSearch.minCost + " ";
                if(dishSearch.order != null) {
                    search += "and ";
                }
            }
            else if(dishSearch.maxCost != null) {
                search += "cost < " + dishSearch.maxCost + " ";
                if(dishSearch.order != null) {
                    search += "and ";
                }
            }
            if(dishSearch.order != null) {
                if(dishSearch.order == 1) {
                    search += "cost > 0 order by cost desc";
                }
                else if(dishSearch.order == 2) {
                    search += "cost > 0 order by cost asc";
                }
            }
            search += ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(search);
            List<Dish> list = new ArrayList<>();
            while (resultSet.next()) {
                Dish dish = new Dish();
                dish.setIdDish(resultSet.getLong("id_dish"));
                dish.setTitle(resultSet.getString("title"));
                dish.setWeight(resultSet.getInt("weight"));
                dish.setCategory(resultSet.getString("category"));
                dish.setCost(resultSet.getInt("cost"));
                getIngredients(dish);
                getCooks(dish);
                list.add(dish);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getIngredients(Dish dish) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select ing.id_ingredient from ingredient ing " +
                            "inner join ingredientToDish ingToDi " +
                            "on ingToDi.id_ingredient = ing.id_ingredient " +
                            "where ingToDi.id_dish = " + dish.getIdDish() + " ;");
            List<Ingredient> ingredients = new ArrayList<>();
            while (resultSet.next()) {
                ingredients.add(ingredientDAO.get(resultSet.getLong("id_ingredient")));
            }
            dish.setIngredients(ingredients);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void getCooks(Dish dish) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select cook.id_cook from cook cook " +
                            "inner join cookFromDish cookFromDi " +
                            "on cookFromDi.id_cook = cook.id_cook " +
                            "where cookFromDi.id_dish = " + dish.getIdDish() + " ;");
            List<Cook> cooks = new ArrayList<>();
            while (resultSet.next()) {
                cooks.add(cookDAO.get(resultSet.getLong("id_cook")));
            }
            dish.setCooks(cooks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Dish get(Long key) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select id_dish, title, weight, category, cost " +
                    "from dish where id_dish = ?;");
            statement.setLong(1, key);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Dish dish = new Dish();
                dish.setIdDish(resultSet.getLong("id_dish"));
                dish.setTitle(resultSet.getString("title"));
                dish.setWeight(resultSet.getInt("weight"));
                dish.setCategory(resultSet.getString("category"));
                dish.setCost(resultSet.getInt("cost"));
                getIngredients(dish);
                getCooks(dish);
                return dish;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    @Override
    public Long save(Dish dish) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select id_dish from dish where id_dish = ?;");
            statement.setLong(1, dish.getIdDish());
            ResultSet id = statement.executeQuery();
            if(id.next()) {
                statement = connection.prepareStatement(
                        "update from dish " +
                                "set title = ?, weight = ?, category = ?, cost = ? " +
                                "where id_dish = ?;");
            }
            else {
                statement = connection.prepareStatement(
                        "insert into dish (title, weight, category, cost, id_dish) " +
                                "values (?, ?, ?, ?);");
            }
            statement.setString(1, dish.getTitle());
            statement.setInt(2, dish.getWeight());
            statement.setString(3, dish.getCategory());
            statement.setInt(4, dish.getCost());
            statement.setLong(5, dish.getIdDish());
            statement.execute();

            ResultSet resultSet = statement.executeQuery(
                    "select ing.id_ingredient from ingredient ing " +
                            "inner join ingredientToDish ingToDi " +
                            "on ingToDi.id_ingredient = ing.id_ingredient " +
                            "where ingToDi.id_dish = " + dish.getIdDish() + " ;");
            List<Long> idIngredients = new ArrayList<>();
            while (resultSet.next()) {
                idIngredients.add(resultSet.getLong("id_ingredient"));
            }
            for(Ingredient ingredient : dish.getIngredients()) {
                ingredientDAO.save(ingredient);
                if(!idIngredients.contains(ingredient.getIdIngredient())) {
                    statement.execute("insert into ingredientToDish (id_ingredient, id_dish) " +
                            "values( " + ingredient.getIdIngredient() + ", " + dish.getIdDish() + " );");
                }
            }
            resultSet = statement.executeQuery(
                    "select cook.id_cook from cook cook " +
                            "inner join cookFromDish cookFromDi " +
                            "on cookFromDi.id_cook = cook.id_cook " +
                            "where cookFromDi.id_dish = " + dish.getIdDish() + " ;");
            List<Long> idCooks = new ArrayList<>();
            while(resultSet.next()) {
                idCooks.add(resultSet.getLong("id_cook"));
            }
            for(Cook cook : dish.getCooks()) {
                cookDAO.save(cook);
                if(!idCooks.contains(cook.getIdCook())) {
                    statement.execute("insert into cookFromDish (id_cook, id_dish) " +
                            "values( " + cook.getIdCook() + ", " + dish.getIdDish() + " );");
                }
            }
            return dish.getIdDish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
