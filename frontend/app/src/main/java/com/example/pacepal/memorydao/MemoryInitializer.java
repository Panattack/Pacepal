package com.example.pacepal.memorydao;

import com.example.pacepal.dao.DAOFactory;
import com.example.pacepal.dao.Initializer;
import com.example.pacepal.dao.ResultDAO;

public class MemoryInitializer extends Initializer {
    /**
     * Initialize the dao factory & the daos
     */
    public MemoryInitializer() {
        System.setProperty("daofactory", "com.example.pacepal.memorydao.MemoryDAOFactory");
    }

    /**
     * Get the result dao that contains all the results
     * @return the static result dao
     */
    @Override
    public ResultDAO getResultDAO() {
        return DAOFactory.getFactory().getResultDAO();
    }
}
