import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;

public class Worker extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket requestSocket;
    ArrayList<Waypoint> chunk;
    int id;
    
    public Worker(int work_id){
        this.id = work_id;
    }
    
    public void run() {
        try {
            String host = "localhost";
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, 4321);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            Chunk c = (Chunk) in.readObject();
            System.out.println("Cunk id is : " + c.id + "Worker_id : " + this.id);
            // Worker job - Map function
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);// need it in readObject 
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        String fileName = "pacepal/src/config.conf";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found !!!");; // FileNotFoundException catch is optional and can be collapsed
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < Integer.parseInt(prop.getProperty("num_of_workers")); i++)
        {
            new Worker(i).start();
        }
    }
}
