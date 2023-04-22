import java.io.*;
import java.net.*;

public class ClientAction extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    // User id
    private int clientId;

    public ClientAction(Socket connection, int id) {
        try {
            in = new ObjectInputStream(connection.getInputStream());
            this.clientId = id;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File receiveFile()
    {
        try {
            // Get the file name from the client
            byte[] fileNameBytes = new byte[1000];
            int bytesRead;
            bytesRead = this.in.read(fileNameBytes);
            String fileName = new String(fileNameBytes, 0, bytesRead);
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n" + fileName + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            // Open a new file for writing
            File receivedFile = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);

            // System.out.println("Receiving file \"" + fileName + "\"...");
            byte[] buffer = new byte[1000];
            int len;
            while ((len = this.in.read(buffer)) != -1) {
                // Write the received data to the file
                fileOutputStream.write(buffer, 0, len);
            }

            // System.out.println("File \"" + fileName + "\" has been received and saved.");

            // Close the file output stream
            fileOutputStream.close();
            return receivedFile;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override 
    public void run()
    {
        
        // Create the user record
        create_user(this.clientId);
        int fileId = 0;
        // Listen all the files that the user send and create threads in order to make the parsing and splitting of the files in parallel
        while (true) {
            File file = receiveFile();;
            Thread t = new ClientConnectionHandler(file, fileId++, this.clientId);
            t.start();
        }
    }

    private void create_user(int user) {
        if (Master.userList.get(user) != null) {
            synchronized (Master.userList) 
            {
                Master.userList.put(user, new User(user));
            }
        }
    }
}
