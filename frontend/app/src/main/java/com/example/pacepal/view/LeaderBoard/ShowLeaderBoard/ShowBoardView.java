package com.example.pacepal.view.LeaderBoard.ShowLeaderBoard;

public interface ShowBoardView {
    /**
     * Create the leaderboard and set the medals
     *
     * @param timesec the time in seconds of the user
     * @param name   the name/id of the user
     * @param num     the position of the user
     */
    void createLeaderBoard(double timesec, int name, int num);

    /**
     * Get the segment id from the previous fragment
     *
     * @return the segment id as an integer
     */
    int getSegmentId();
}
