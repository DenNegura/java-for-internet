package com.example.restaurant.model.DAO;

import java.util.List;

public interface DAO<Entity, Key> {
    public List<Entity> getAll();
    public Entity get(Key key);
    public Key save(Entity entity);
}
