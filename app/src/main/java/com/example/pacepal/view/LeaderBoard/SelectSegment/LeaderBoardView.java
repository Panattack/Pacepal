package com.example.pacepal.view.LeaderBoard.SelectSegment;

import java.util.ArrayList;

public interface LeaderBoardView {
    /**
     * Create the segment list, according to the id that the Master server sent
     *
     * @param num a list of id as integers
     */
    void createSegmentList(ArrayList<Integer> num);

    /**
     * Get the selected segment from the spinner
     *
     * @return selected id as an integer
     */
    int getSelectedSegment();

    /**
     * Moving from the LeaderBoard fragment to ShowBoard Activity
     *
     * @param id the id of the segment
     */
    void sentOption(int id);
}
