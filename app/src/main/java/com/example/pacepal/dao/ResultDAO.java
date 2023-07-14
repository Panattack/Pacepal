package com.example.pacepal.dao;

import com.example.pacepal.model.Results;

import java.util.List;

public interface ResultDAO {
    /**
     * Delete result registered on the dao
     *
     * @param entity result object
     */
    void delete(Results entity);

    /**
     * Register a new result in the dao
     *
     * @param entity result object
     */
    void save(Results entity);

    /**
     * Return all results registered on the dao
     *
     * @return result object list
     */
    List<Results> findAll();
}
