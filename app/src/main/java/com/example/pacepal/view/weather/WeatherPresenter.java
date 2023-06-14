package com.example.pacepal.view.weather;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class WeatherPresenter {
    WeatherView view;
    String host = "192.168.1.6";
    static int fileId;
    private int userId; // 0 by default
    int serverPort = 4321;
    public WeatherPresenter(WeatherView view) {
        this.view = view;
    }
    public void sendCity() {
        String city = view.getText();
        Thread t = new Thread(() -> WeatherProcess(city));
        t.start();
    }

    public void WeatherProcess(String city) {
        ObjectOutputStream out;
        ObjectInputStream in;

        /* Create the streams to send and receive data from server */
        try {
            /* Create socket for contacting the server on port 4321*/
            Socket requestSocket = new Socket(host, serverPort);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> Weather
            out.writeInt(3);
            out.flush();

            out.writeObject(city);
            out.flush();

            HashMap<String, String> weather = (HashMap<String, String>) in.readObject();
            int h = 10;
            requestSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Connection Lost in statistic request");
        } catch (ClassNotFoundException e) {
            System.err.println("Error in connection -- cannot receive statistic object");
        }
    }
}
