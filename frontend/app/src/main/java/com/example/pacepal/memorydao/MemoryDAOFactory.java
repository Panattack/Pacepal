package com.example.pacepal.memorydao;

import com.example.pacepal.dao.DAOFactory;
import com.example.pacepal.dao.ResultDAO;

public class MemoryDAOFactory extends DAOFactory {
    private MemoryResultDAO memoryResultDAO = new MemoryResultDAO();

    /**
     * Επιστρέφει το αντικείμενο για τη διεπαφή {@link ResultDAO}.
     *
     * @return Το αντικείμενο DAO.
     */

    @Override
    public ResultDAO getResultDAO() {
        return memoryResultDAO;
    }
}
