package com.example.pacepal.view.sender;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pacepal.SenderThread;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SenderPresenter {
    String host;
    String path;
    String fileName;
    int serverPort;

    public SenderPresenter() {
//
//        host = prop.getProperty("path");
//        path = prop.getProperty("host");
//        fileName = prop.getProperty("fileName");
//        serverPort = Integer.parseInt(prop.getProperty("serverPort"));
    }

    public void sendFiles(List<String> files) {
        Thread t = new Thread(this::checkWorkerLoad);
        t.start();
//        ArrayList<Thread> threadList = new ArrayList<>();
//
//        for (int i = 0; i < files.size(); i++) {
//            SenderThread sender = new SenderThread(host, 0, files.get(i), serverPort, i, fileName);
//            threadList.add(sender);
//            sender.start();
//        }
//
//        for (Thread t : threadList) {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                System.err.println("Wrong join in ");
//            }
//        }
    }

    private boolean checkWorkerLoad() {

        Socket requestSocket = null;

        try {
            /* Create socket for contacting the server on port 4321 */
            requestSocket = new Socket("192.168.43.139", 4321);
            /* Create the streams to send and receive data from server */
            ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());
            // Send id request --> File
            out.writeInt(0);
            out.flush();

            int msg = in.readInt();
            return msg == 1;
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }
}
