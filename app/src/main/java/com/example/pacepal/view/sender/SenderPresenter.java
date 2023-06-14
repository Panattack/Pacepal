package com.example.pacepal.view.sender;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.pacepal.SenderThread;
import com.example.pacepal.dao.Initializer;
import com.example.pacepal.memorydao.MemoryInitializer;
import com.example.pacepal.model.Results;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class SenderPresenter {
    String host = "192.168.1.6";
    static int fileId;
    private int userId; // 0 by default
    private final Initializer init;
    int serverPort = 4321;
    SenderFragmentView view;
    private List<File> inputList;
    protected boolean checking;

    public SenderPresenter(SenderFragmentView view) {
        this.view = view;
        this.inputList = new ArrayList<>();
        this.init = new MemoryInitializer();
    }

    public void loadFilesFromDownloadFolder() {
        String downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File downloadFolder = new File(downloadFolderPath);

        ArrayList<String> titles = new ArrayList<>();

        if (downloadFolder.exists() && downloadFolder.isDirectory()) {
            File[] files = downloadFolder.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    // You can add additional filters here if needed (e.g., file extension)
                    titles.add(file.getName());
                    inputList.add(file);
                }
            } else {
                view.popMsg("No files found in the download folder");
            }
        } else {
            view.popMsg("Download folder not found");
        }
        view.showFiles(titles);
    }

    private void sendFile(File file, ObjectOutputStream out) {
        try {
            byte[] buffer = new byte[(int) file.length()];
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
            reader.read(buffer, 0, buffer.length);
            reader.close();

            out.writeInt(buffer.length);
            out.flush();
            out.write(buffer, 0, buffer.length);
            out.flush();

        } catch (Exception e) {
            System.err.println("An error occurred in the contexts in sendFile");
        }
    }

    public void submit() throws InterruptedException {
        Thread t = new Thread(this::checkWorkerLoad);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new InterruptedException("Error in checking worker buffer");
        }
        if (checking) {
            ArrayList<String> names = view.submitClicked();
            ArrayList<Thread> threadList = new ArrayList<>();
            for (File file : this.inputList) {
                if (names.contains(file.getName())) {
                    Thread sender = new Thread(() -> fileProcess(file, fileId++));
                    sender.start();
                    threadList.add(sender);
                }
            }
            for (Thread thread : threadList)
                thread.join();

        } else {
            view.alertBox("Error", "No available workers");
        }
    }

    private void checkWorkerLoad() {
        Socket requestSocket = null;

        try {
            /* Create socket for contacting the server on port 4321 */
            // TODO Maybe it will go to the config or will be set dynamically from us
            requestSocket = new Socket(host, 4321);
            /* Create the streams to send and receive data from server */
            ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());
            // Send id request --> File
            out.writeInt(0);
            out.flush();

            int msg = in.readInt();
            checking = msg == 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fileProcess(File file, int id) {
        // Convert it to byte stream
        // Create a new thread and send it
        Socket requestSocket = null;

        try {
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, serverPort);
            /* Create the streams to send and receive data from server */
            ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> File
            out.writeInt(1);
            out.flush();

            // Send user id
            out.writeInt(this.userId);
            out.flush();

            //Send file id
            out.writeInt(id);
            out.flush();

            //Sending GPX file
            sendFile(file, out);

            // Route statistics
            HashMap<String, Double> results = (HashMap<String, Double>) in.readObject();
            synchronized (this.init.getResultDAO()) {
                Results res = new Results((new Double(results.get("gpxID"))).intValue(), (new Double(results.get("userID"))).intValue(), results.get("totalDistance"), results.get("avgSpeed"), results.get("totalElevation"), results.get("totalTime"));
                init.getResultDAO().save(res);
            }
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            System.err.println("Error: unusual context --> \"in\"  or \"out\" or writing in the file to run");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: ClassNotFound in \"in\" to receiving result");
        }
    }

}
