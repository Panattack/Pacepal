package com.example.pacepal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import android.os.Handler;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.example.pacepal.model.Results;


public class SenderThread extends Thread {

    private static String fileName;
    private String gpx;
    Handler handler;
    private int fileId;

    ArrayList<Thread> threadList = new ArrayList<Thread>();
    ObjectOutputStream out = null ;
    ObjectInputStream in = null ;
    private static Object lock_msg = new Object();


    public SenderThread(String arg ,int fileIndex, Handler handler){
        this.gpx = arg;
        this.fileId = fileIndex;
        this.handler= handler;
    }

    public void run(){
        try {
            Socket requestSocket = null;
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket("localhost", 4321);
            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> File
            out.writeInt(1);
            out.flush();

            int msg = in.readInt();
            synchronized (SenderThread.lock_msg)
            {
                if (msg == 0)
                {
                    System.out.println("Not enough workers... Try again");
                    SenderThread.lock_msg.notify();
                    return;
                }
                else
                {
                    System.out.println("Enough workers --> approved");
                    SenderThread.lock_msg.notify();
                }
            }

            // TODO Send user id
           // out.writeInt(SenderThread.userId);
            //out.flush();

            //Send file id
            out.writeInt(this.fileId);
            out.flush();

            //sendFile(this.gpx, out);
            // TODO Sending GPX file

            // Route statistics
            Results results = (Results) in.readObject();

            // TODO Write the results in a list
            //synchronized (Client.writer)
            //{
              //  Client.writer = new BufferedWriter(new FileWriter(fileName, true));
                //Client.writer.write(results.toString());
                //Client.writer.close();
            //}

            in.close(); out.close();
            requestSocket.close();


        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            System.err.println("Error: unusual context --> \"in\"  or \"out\" or writing in the file to run");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
