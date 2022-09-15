package com.example.restaurant.model.DAO;

import com.example.restaurant.model.entity.Cook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CookDAO implements DAO<Cook, Long> {

    @Autowired
    Connection connection;
    @Override
    public List<Cook> getAll() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select " +
                    "id_cook, name, surname, date from cook;");
            List<Cook> list = new ArrayList<>();
            while (resultSet.next()) {
                Cook cook = new Cook();
                cook.setIdCook(resultSet.getLong("id_cook"));
                cook.setName(resultSet.getString("name"));
                cook.setSurname(resultSet.getString("surname"));
                cook.setDate(resultSet.getDate("date"));
                list.add(cook);
            }
            return list;
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    @Override
    public Cook get(Long key) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select id_cook, name, surname, date from cook where id_cook = ?;");
            statement.setLong(1, key);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Cook cook = new Cook();
                cook.setIdCook(resultSet.getLong("id_cook"));
                cook.setName(resultSet.getString("name"));
                cook.setSurname(resultSet.getString("surname"));
                cook.setDate(resultSet.getDate("date"));
                return cook;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    @Override
    public Long save(Cook cook) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select id_cook from cook where id_cook = ?;");
            statement.setLong(1, cook.getIdCook());
            ResultSet id = statement.executeQuery();
            if(id.next()) {
                statement = connection.prepareStatement(
                        "update from cook " +
                                "set name = ?, surname = ?, date = ?" +
                                "where id_cook = ?;");
            }
            else {
                statement = connection.prepareStatement(
                        "insert into ingredient (name, surname, date, id_cook) " +
                                "values (?, ?, ?, ?);");
            }
            statement.setString(1, cook.getName());
            statement.setString(2, cook.getSurname());
            statement.setDate(3, cook.getDate());
            statement.setLong(4, cook.getIdCook());
            statement.execute();
            return cook.getIdCook();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
