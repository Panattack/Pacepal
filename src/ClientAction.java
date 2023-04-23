import java.io.*;
import java.net.*;

public class ClientAction extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    private InputStream is;
    private Socket socket;
    private int num_files;
    // User id
    private int clientId;

    public ClientAction(Socket connection, int id) {
        try {
            this.socket = connection;
            in = new ObjectInputStream(connection.getInputStream());
            this.clientId = id;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] concatenateByteArrays(byte[] array1, byte[] array2, int length) {
        byte[] result = new byte[array1.length + length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, length);
        return result;
    }

    private void receiveFile()
    {
        try {
            // Check if the socket is connected
            // if (socket.isConnected()) {
            //     System.out.println("Socket is connected.");
            // } else {
            //     System.out.println("Socket is not connected.");
            // }

            // // Check if the socket is closed
            // if (socket.isClosed()) {
            //     System.out.println("Socket is closed.");
            // } else {
            //     System.out.println("Socket is not closed.");
            // }
            // Get the file name from the client
            String filename = (String) in.readObject();
        
            int fileSize = in.readInt();
            // FileOutputStream fos = new FileOutputStream("./filesReceived/" + filename);
            byte[] buffer = new byte[fileSize];
            byte[] fileBytes = new byte[0];
            int bytesRead;

            while (fileBytes.length < fileSize) {
                bytesRead = in.read(buffer);
                // fos.write(buffer, 0, bytesRead);
                fileBytes = concatenateByteArrays(fileBytes, buffer, bytesRead);
            }
            // fos.close();
            this.is = new ByteArrayInputStream(fileBytes);
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override 
    public void run()
    {
        // Create the user record
        create_user(this.clientId);
        setNumFiles();
        int fileId = 0;
        // Listen all the files that the user send and create threads in order to make the parsing and splitting of the files in parallel
        for (int i = 0; i < this.num_files; i++)
        {
            receiveFile();
            InputStream file = this.is;
            Thread t = new ClientConnectionHandler(file, fileId++, this.clientId);
            t.start();
        }
        
    }

    private void setNumFiles() 
    {
        try {
            this.num_files = this.in.readInt();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
