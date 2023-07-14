package com.example.pacepal.view.sender;

import java.util.ArrayList;

public interface SenderFragmentView {
    /**
     * Shows files with checkboxes to select
     *
     * @param files an arraylist that contains the titles of each file gpx
     */
    public void showFiles(ArrayList<String> files);

    /**
     * Pops a message when an event occurs
     *
     * @param message the message as a string
     */
    public void popMsg(String message);

    /**
     * Shows an alert box when an event occurs
     *
     * @param title
     * @param message
     */
    public void alertBox(String title, String message);

    /**
     * After "submit" is clicked, it returns the names of the files that were selected
     *
     * @return the names of the files that were selected as strings
     */
    public ArrayList<String> submitClicked();
}
