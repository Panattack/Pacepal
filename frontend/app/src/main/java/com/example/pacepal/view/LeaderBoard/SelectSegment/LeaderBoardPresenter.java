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

    /**
     * Constructor that initializes the variables
     *
     * @param view   the view that will be used to call the methods in the activity
     * @param host   the ip of the host as a string
     * @param server the port of the host as an integer
     */
    public LeaderBoardPresenter(LeaderBoardView view, String host, int server) {
        this.view = view;
        this.init = new MemoryInitializer();
        this.host = host;
        this.serverPort = server;
    }

    /**
     * Trying to get the maximum id of the segments and establishing a connection with the Master server.
     * In case of failure, a message pops up
     *
     * @throws InterruptedException
     */
    public void getNumber() throws InterruptedException {
        Thread k = new Thread(this::SegmentNumber);
        k.start();
        try {
            k.join();
        } catch (InterruptedException e) {
            throw new InterruptedException("Error in getting number of segments");
        }
        setList();
    }

    /**
     * Set the list in order to pass it in the spinner
     */
    private void setList() {
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 1; i <= numberSegment; i++) {
            choices.add(i);
        }
        view.createSegmentList(choices);
    }

    /**
     * Get the maximum id of the segments
     */
    private void SegmentNumber() {

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

    /**
     * Get and send the selected id segment to the next activity
     */
    protected void sendNumber() {
        int choice = view.getSelectedSegment();
        view.sentOption(choice);
    }
}
