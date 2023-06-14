package com.example.pacepal.memorydao;

import com.example.pacepal.model.Results;
import com.example.pacepal.dao.ResultDAO;

import java.util.ArrayList;
import java.util.List;

public class MemoryResultDAO implements ResultDAO {
    protected static List<Results> entities = new ArrayList<>();
    @Override
    public void delete(Results entity) {
        entities.remove(entity);
    }

    @Override
    public void save(Results entity) {
        if (!entities.contains(entity))
            entities.add(entity);
    }

    @Override
    public List<Results> findAll() {
        return new ArrayList<>(entities);
    }
}
