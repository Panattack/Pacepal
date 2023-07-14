package com.example.pacepal.view.weather;

import com.example.pacepal.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class WeatherPresenter {
    WeatherView view;
    String host;
    static int fileId;
    private int userId; // 0 by default
    HashMap<String, String> weather;
    int serverPort;

    /**
     * Constructor that initializes all attributes
     *
     * @param view       the view that will be used to call the methods in the activity
     * @param serverPort the port of the Master server as an integer
     * @param host       the ip address of the Master server as a string
     */
    public WeatherPresenter(WeatherView view, int serverPort, String host) {
        this.host = host;
        this.serverPort = serverPort;
        weather = new HashMap<>();
        this.view = view;
    }

    /**
     * Start a connection with the Master server, asking the weather about the declared city
     */
    public void sendCity() {
        String city = view.getText();
        Thread t = new Thread(() -> WeatherProcess(city));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("No city found");
        }
        setIcon();
    }

    /**
     * Establish a connection with the Master server and receive the weather info
     *
     * @param city
     */
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

            weather = (HashMap<String, String>) in.readObject();
            requestSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Connection Lost in statistic request");
        } catch (ClassNotFoundException e) {
            System.err.println("Error in connection -- cannot receive statistic object");
        }
    }

    /**
     * Pass all necessary weather info to the activity
     */
    private void setIcon() {
        String id = weather.get("id");
        assert id != null;
        if (id.startsWith("2"))
            view.setImage(R.drawable._1_thunderstorm);
        else if (id.startsWith("2"))
            view.setImage(R.drawable._9_shower_rain);
        else if (id.startsWith("5")) {
            if (id.equals("500") || id.equals("501") || id.equals("502") || id.equals("503") || id.equals("504"))
                view.setImage(R.drawable._0_light_rain);
            else if (id.equals("511"))
                view.setImage(R.drawable._3_snow);
            else
                view.setImage(R.drawable._9_shower_rain);
        } else if (id.startsWith("6"))
            view.setImage(R.drawable._3_snow);
        else if (id.startsWith("7"))
            view.setImage(R.drawable._0_mist);
        else if (id.equals("800"))
            view.setImage(R.drawable._1_clear_sky);
        else if (id.startsWith("80")) {
            if (id.equals("801"))
                view.setImage(R.drawable._2_few_clouds);
            else if (id.equals("802"))
                view.setImage(R.drawable._3_scattered_clouds);
            else if (id.equals("803") || id.equals("804"))
                view.setImage(R.drawable._4_broken_clouds);
        }

        view.setStatus(weather.get("temp"), weather.get("pressure"), weather.get("humidity"), weather.get("main"), weather.get("description"));
    }
}
