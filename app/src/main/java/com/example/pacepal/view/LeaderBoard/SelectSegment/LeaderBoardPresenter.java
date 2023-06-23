package com.example.pacepal.view.LeaderBoard.SelectSegment;

import android.util.Log;

import com.example.pacepal.dao.Initializer;
import com.example.pacepal.memorydao.MemoryInitializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class LeaderBoardPresenter {

    String host;
    int serverPort;
    private final Initializer init;
    LeaderBoardView view;


    protected int numberSegment;

    public LeaderBoardPresenter(LeaderBoardView view,String host, int server){
        this.view= view;
        this.init= new MemoryInitializer();
        this.host = host;
        this.serverPort= server;

    }

    public void getNumber()throws InterruptedException{
        Thread k = new Thread(this::SegmentNumber);
        k.start();
        try {
            k.join();
        } catch (InterruptedException e) {
            throw new InterruptedException("Error in getting number of segments");
        }
        setList();

    }

    private void setList(){
        ArrayList<Integer> choices = new ArrayList<>();
        for(int i =1 ; i<= numberSegment;i++){
            choices.add(i);
        }

        view.createSegmentList(choices);
    }

    private void SegmentNumber(){

        Socket requestSocket = null;

        try {
            /* Create socket for contacting the server on port 4321 */

            requestSocket = new Socket(host, 4321);
            /* Create the streams to send and receive data from server */
            ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> Segment
            out.writeInt(5);
            out.flush();

            // We take the number of the segments
            numberSegment = in.readInt();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendNumber(){
        int choice = view.getSelectedSegment();
        view.sentOption(choice);
    }



}
