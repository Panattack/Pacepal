package com.example.pacepal.memorydao;

import com.example.pacepal.model.Results;
import com.example.pacepal.dao.ResultDAO;

import java.util.ArrayList;
import java.util.List;

public class MemoryResultDAO implements ResultDAO {
    protected static List<Results> entities = new ArrayList<>();

    /**
     * Delete result registered on the dao
     *
     * @param entity result object
     */
    @Override
    public void delete(Results entity) {
        entities.remove(entity);
    }

    /**
     * Register a new result in the dao
     *
     * @param entity result object
     */
    @Override
    public void save(Results entity) {
        if (!entities.contains(entity))
            entities.add(entity);
    }

    /**
     * Return all results registered on the dao
     *
     * @return result object list
     */
    @Override
    public List<Results> findAll() {
        return new ArrayList<>(entities);
    }
}
