package com.example.pacepal.dao;

public abstract class DAOFactory {
    private static DAOFactory factory = null;

    /**
     * Επιστρέφει το εργοστάσιο για την παραγωγή των
     * αντικειμένων DAO.
     *
     * @return To εργοστάσιο για την παραγωγή των DAO αντικειμένων
     */
    public static DAOFactory getFactory() {
        if (factory == null) {
            String className = null;

            if (System.getProperty("daofactory") != null) {
                className = System.getProperty("daofactory");
            }

            try {
                factory = (DAOFactory) Class.forName(className).newInstance();
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
        return factory;
    }

    /**
     * Επιστρέφει το αντικείμενο για τη διεπαφή {@link ResultDAO}.
     *
     * @return Το αντικείμενο DAO.
     */
    public abstract ResultDAO getResultDAO();

}
