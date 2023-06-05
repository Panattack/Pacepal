package com.example.pacepal;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.os.Handler;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.example.pacepal.messages.Results;
import com.example.pacepal.model.Client;

public class SenderThread extends Thread {
    static BufferedWriter writer;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private final String host;
    private final int userId;
    private final String gpx;
    private final int serverPort;
    private final int fileId;

    public SenderThread(String host, int userId, String gpx, int serverPort, int fileId) {
        this.host = host;
        this.userId = userId;
        this.gpx = gpx;
        this.serverPort = serverPort;
        this.fileId = fileId;
    }

    private void sendFile(String fName, ObjectOutputStream out) {
        try {
            File file = new File(fName);
            byte[] buffer = new byte[(int) file.length()];
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
            reader.read(buffer, 0, buffer.length);
            reader.close();

            out.writeInt(buffer.length);
            out.flush();
            out.write(buffer, 0, buffer.length);
            out.flush();

        } catch (Exception e) {
            System.err.println("An error occured in the contexts in sendFile");
        }
    }

    @Override
    public void run() {
        Socket requestSocket = null;

        try {
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, serverPort);
            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> File
            out.writeInt(1);
            out.flush();

            int msg = in.readInt();

            // Send user id
            out.writeInt(userId);
            out.flush();

            //Send file id
            out.writeInt(this.fileId);
            out.flush();

            sendFile(this.gpx, out);
            //Sending GPX file

            // Route statistics
            Results results = (Results) in.readObject();

            in.close();
            out.close();
            requestSocket.close();

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            System.err.println("Error: unusual context --> \"in\"  or \"out\" or writing in the file to run");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: ClassNotFound in \"in\" to receiving result");
        }
    }
}
