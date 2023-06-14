package com.example.pacepal.memorydao;

import com.example.pacepal.dao.DAOFactory;
import com.example.pacepal.dao.Initializer;
import com.example.pacepal.dao.ResultDAO;

public class MemoryInitializer extends Initializer {
    public MemoryInitializer() {
        System.setProperty("daofactory", "com.example.pacepal.memorydao.MemoryDAOFactory");
    }
    @Override
    public ResultDAO getResultDAO() {
        return DAOFactory.getFactory().getResultDAO();
    }
}
