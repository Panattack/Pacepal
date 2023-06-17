package com.example.pacepal.view.LeaderBoard.SelectSegment;

import android.util.Log;

import com.example.pacepal.dao.Initializer;
import com.example.pacepal.memorydao.MemoryInitializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LeaderBoardPresenter {

    String host = "192.168.1.3";
    int serverPort = 4321;
    private final Initializer init;
    LeaderBoardView view;

    protected int numberSegment;

    public LeaderBoardPresenter(LeaderBoardView view){
        this.view= view;
        this.init= new MemoryInitializer();

    }

    public void getNumber()throws InterruptedException{
        Thread k = new Thread(this::SegmentNumber);
        k.start();
        try {
            k.join();
        } catch (InterruptedException e) {
            throw new InterruptedException("Error in getting number of segments");
        }
        setHint();
    }

    private void setHint(){
        view.segmentHint(numberSegment);
    }

    private void SegmentNumber(){

        Socket requestSocket = null;

        try {
            /* Create socket for contacting the server on port 4321 */

            requestSocket = new Socket(host, 4321);
            /* Create the streams to send and receive data from server */
            ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> File
            out.writeInt(4);
            out.flush();

            // We take the number of the segments
            numberSegment = in.readInt();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendNumber(){
        view.sentOption();
    }

    protected boolean checker(int answer){
        if(answer>0 & answer<= numberSegment){
            return true;
        }
        return false;

    }

    public int getNum(){
        return numberSegment;
    }
}
