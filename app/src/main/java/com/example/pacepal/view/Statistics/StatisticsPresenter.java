package com.example.pacepal.view.Statistics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class StatisticsPresenter {
    String host = "192.168.1.2";
    private int userId; // 0 by default
    int serverPort = 4321;
    private HashMap<String, Double> stats;
    StatisticsView view;
    public StatisticsPresenter(StatisticsView view) {
        this.view = view;
    }
    public void receiveStatistics() throws InterruptedException {
        Thread t = new Thread(this::StatisticsProcess);
        t.start();
        try {
            t.join();
            if (stats == null)
                view.alertBox("Warning", "You haven't registered in our app. Please do so by choosing \"Send files\" !");
            else {
                view.createTimeBar(stats);
                view.createDistanceBar(stats);
                view.createElevationBar(stats);
            }
        } catch (InterruptedException e) {
            throw new InterruptedException("Fault with receiving statistics");
        }
    }

    private void StatisticsProcess() {
        ObjectOutputStream out;
        ObjectInputStream in;

        /* Create the streams to send and receive data from server */
        try {
            /* Create socket for contacting the server on port 4321*/
            Socket requestSocket = new Socket(host, serverPort);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> Statistics
            out.writeInt(2);
            out.flush();

            // Send user id to check if there is a user in the database
            out.writeInt(userId);
            out.flush();

            int msg = in.readInt();

            if (msg == 0)
            {
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
