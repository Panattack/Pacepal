package com.example.pacepal.view.sender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SenderPresenter {
    String host;
    String path;
    String fileName;
    int serverPort;
    public SenderPresenter() {
        Properties prop = new Properties();
        // String configName = "pacepal/config/client.cfg";
        String configName = "config/client.cfg";

        try (FileInputStream fis = new FileInputStream(configName)) {
            prop.load(fis);
        } catch (IOException ex) {
            System.out.println("File not found !!!");
        }
        host = prop.getProperty("path");
        path = prop.getProperty("host");
        fileName = prop.getProperty("fileName");
        serverPort = Integer.parseInt(prop.getProperty("serverPort"));
    }

    public void sendFiles(ArrayList<String> files) {

    }
}
