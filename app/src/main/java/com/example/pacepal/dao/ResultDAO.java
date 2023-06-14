package com.example.pacepal.dao;

import com.example.pacepal.model.Results;

import java.util.List;

public interface ResultDAO {
    void delete(Results entity);
    void save(Results entity);
    List<Results> findAll();
}
