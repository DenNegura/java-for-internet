package com.example.restaurant.model.DAO;

import com.example.restaurant.model.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IngredientDAO implements DAO<Ingredient, Long> {

    @Autowired
    Connection connection;
    @Override
    public List<Ingredient> getAll() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select " +
                    "id_ingredient, title, type " +
                    "from ingredient;");
            List<Ingredient> list = new ArrayList<>();
            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIdIngredient(resultSet.getLong("id_ingredient"));
                ingredient.setTitle(resultSet.getString("title"));
                ingredient.setType(resultSet.getString("type"));
                list.add(ingredient);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Map<String, List<Ingredient>> getSortedByType() {
        List<String> typesOfIngredients = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select type from ingredient group by type;");
            while (resultSet.next()) {
                typesOfIngredients.add(resultSet.getString("type"));
            }
            List<Ingredient> listOfIngredients = getAll();
            Map<String, List<Ingredient>> map = new HashMap<String, List<Ingredient>>();
            for(String type : typesOfIngredients) {
                map.put(type, new ArrayList<Ingredient>());
                for(Ingredient ingredient : listOfIngredients) {
                    if(ingredient.getType().equals(type)) {
                        map.get(type).add(ingredient);
                    }
                }
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Ingredient get(Long key) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select id_ingredient, title, type " +
                            "from ingredient where id_ingredient = ?;");
            statement.setLong(1, key);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIdIngredient(resultSet.getLong("id_ingredient"));
                ingredient.setTitle(resultSet.getString("title"));
                ingredient.setType(resultSet.getString("type"));
                return ingredient;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    @Override
    public Long save(Ingredient ingredient) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select id_ingredient from ingredient where id_ingredient = ?;");
            statement.setLong(1, ingredient.getIdIngredient());
            ResultSet id = statement.executeQuery();
            if(id.next()) {
                statement = connection.prepareStatement(
                        "update from ingredient " +
                                "set title = ?, type = ? " +
                                "where id_ingredient = ?;");
            }
            else {
                statement = connection.prepareStatement(
                        "insert into ingredient (title, type, id_ingredient) " +
                                "values (?, ?, ?);");
            }
            statement.setString(1, ingredient.getTitle());
            statement.setString(2, ingredient.getType());
            statement.setLong(3, ingredient.getIdIngredient());
            statement.execute();
            return ingredient.getIdIngredient();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
