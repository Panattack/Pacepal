package com.example.pacepal.view.results;

import java.util.HashMap;

public interface ResultsView {
    /**
     * Shows the results in a formatted way
     *
     * @param results a hashmap with key the id of the attribute and value the attribute
     */
    void viewResults(HashMap<String, String> results);
}
