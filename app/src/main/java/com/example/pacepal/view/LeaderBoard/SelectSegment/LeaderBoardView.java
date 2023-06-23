package com.example.pacepal.view.LeaderBoard.SelectSegment;

import java.util.ArrayList;

public interface LeaderBoardView {
    void createSegmentList(ArrayList<Integer> num);
    int getSelectedSegment();
    void sentOption(int id);
}
