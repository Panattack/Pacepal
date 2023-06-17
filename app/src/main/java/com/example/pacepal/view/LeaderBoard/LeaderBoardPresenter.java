package com.example.pacepal.view.LeaderBoard;

import java.util.ArrayList;

public class LeaderBoardPresenter {

    ArrayList<Double> times ;
    ArrayList<String> names;
    LeaderBoardView view;

    public LeaderBoardPresenter(LeaderBoardView view){
        this.view= view;
        //TODO : DELETE LATER
        times = new ArrayList<>();
        names = new ArrayList<>();

        times.add(25.6);
        times.add(24.7);
        times.add(22.4);
        times.add(21.3);

        names.add("Lydia");
        names.add("Gwgw");
        names.add("Panos");
        names.add("John");



    }

    public void createBoard(){


        for(int i =1 ; i<=4; i++){
            view.createLeaderBoard(times.get(i-1),names.get(i-1),i);
        }


    }

}
