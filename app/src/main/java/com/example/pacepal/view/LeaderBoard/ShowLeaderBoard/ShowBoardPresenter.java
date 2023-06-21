package com.example.pacepal.view.LeaderBoard.ShowLeaderBoard;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ShowBoardPresenter {
    String host = "192.168.1.2";
    int serverPort = 4321;
    ShowBoardView view;
    HashMap<Integer,Double> listValues;
    HashMap<Integer,Integer> listPosition;


    public ShowBoardPresenter(ShowBoardView view){

        this.view=view;
        listValues = new HashMap<>();
        listPosition = new HashMap<>();
    }

    public void createBoard(){
        for (int i =0; i<listValues.size();i++){


            int user_id = listPosition.get(i+1);
            double time = listValues.get(user_id);
            view.createLeaderBoard(time,user_id,i);
        }


    }

    public void show() throws InterruptedException{
        Thread k = new Thread (this::getList);
        k.start();
        try {
            k.join();
        } catch (InterruptedException e) {
            throw new InterruptedException("Error in checking worker buffer");
        }
    }
    private void getList(){

        Socket requestSocket = null;

        try {
            /* Create socket for contacting the server on port 4321 */

            requestSocket = new Socket(host, serverPort);
            /* Create the streams to send and receive data from server */
            ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> Segment
            out.writeInt(4);
            out.flush();

            // we send the segmentId
            out.writeInt(view.getSegmentId()-1);// the first segmentId is 0
            out.flush();
            // We take the number of the segments

            // <UserId , Value>
             listValues =(HashMap<Integer,Double>) in.readObject();
             Log.e("DEBUGG", String.valueOf(listValues.size()));

             // <Position , UserId>
             listPosition =  (HashMap<Integer,Integer>) in.readObject();
              Log.e("DEBUGG", String.valueOf(listPosition.size()));



        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
