package com.example.pacepal.view.Statistics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class StatisticsPresenter {
    String host;
    private int userId; // 0 by default
    int serverPort;
    private HashMap<String, Double> stats;
    StatisticsView view;

    /**
     * Constructor that initializes the attributes
     *
     * @param view       the view that will be used to call the methods in the activity
     * @param serverPort the port of the Master server as an integer
     * @param host       the ip address of the Master server as a string
     * @param id         the id of the user as an integer
     */
    public StatisticsPresenter(StatisticsView view, int serverPort, String host, int id) {
        this.userId = id;
        this.host = host;
        this.serverPort = serverPort;
        this.view = view;
    }

    /**
     * Creates the charts
     *
     * @throws InterruptedException in case something goes wrong with the connection
     */
    public void receiveStatistics() throws InterruptedException {
        Thread t = new Thread(this::StatisticsProcess);
        t.start();
        try {
            t.join();
            if (stats == null)
                view.alertBox("Warning", "You haven't registered in our app. Please do so by choosing \"Submit\" !");
            else {
                view.createTimeBar(stats);
                view.createDistanceBar(stats);
                view.createElevationBar(stats);
            }
        } catch (InterruptedException e) {
            throw new InterruptedException("Fault with receiving statistics");
        }
    }

    /**
     * Creates a connection between the Master server and the app in order to receive the statistics of the user and the global statistics
     */
    private void StatisticsProcess() {
        ObjectOutputStream out;
        ObjectInputStream in;

        /* Create the streams to send and receive data from server */
        try {
            /* Create socket for contacting the server on port 4321*/
            Socket requestSocket = new Socket(host, serverPort);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request
            out.writeInt(2);
            out.flush();

            // Send user id to check if there is a user in the database
            out.writeInt(userId);
            out.flush();

            int msg = in.readInt();

            if (msg == 0) {
                // There is no user in the database
                requestSocket.close();
                return;
            }

            stats = (HashMap<String, Double>) in.readObject();

            requestSocket.close();
        } catch (IOException e) {
            System.err.println("Connection Lost in statistic request");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
