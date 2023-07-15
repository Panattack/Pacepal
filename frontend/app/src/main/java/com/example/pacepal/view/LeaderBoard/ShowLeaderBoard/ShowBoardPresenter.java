package com.example.pacepal.view.LeaderBoard.ShowLeaderBoard;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ShowBoardPresenter {
    String host;
    int serverPort;
    ShowBoardView view;
    HashMap<Integer, Double> listValues;
    HashMap<Integer, Integer> listPosition;

    /**
     * Constructor that intializes the variables
     *
     * @param view   the view that will be used to call the methods in the activity
     * @param host   the ip of the host as a string
     * @param server the port of the host as an integer
     */
    public ShowBoardPresenter(ShowBoardView view, String host, int server) {
        this.view = view;
        listValues = new HashMap<>();
        listPosition = new HashMap<>();
        this.host = host;
        this.serverPort = server;
    }

    /**
     * Creates the leaderboard
     */
    public void createBoard() {
        for (int i = 0; i < listValues.size(); i++) {

            int user_id = listPosition.get(i + 1);
            double time = listValues.get(user_id);
            view.createLeaderBoard(time, user_id, i + 1);
        }
    }

    /**
     * Gets the leaderboard from Master server
     *
     * @throws InterruptedException when a connection is lost
     */
    public void show() throws InterruptedException {
        Thread k = new Thread(this::getList);
        k.start();
        try {
            k.join();
        } catch (InterruptedException e) {
            throw new InterruptedException("Error in checking worker buffer");
        }
    }

    /**
     * Connection between the app and Master server in order th get the leaderboard
     */
    private void getList() {

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
            out.writeInt(view.getSegmentId() - 1);// the first segmentId is 0
            out.flush();
            // We take the number of the segments

            // <UserId , Value>
            listValues = (HashMap<Integer, Double>) in.readObject();

            // <Position , UserId>
            listPosition = (HashMap<Integer, Integer>) in.readObject();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
