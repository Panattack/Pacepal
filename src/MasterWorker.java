import java.io.*;
import java.net.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MasterWorker
{
    ObjectInputStream in;
    ObjectOutputStream out;
    private int n = 15;// DEFINE IN Config file
    private String gpx;
    private ArrayList<Waypoint> wpt_list;

    public MasterWorker(String gpx)
    {
        this.gpx = gpx;
    }
    /* Define the socket that receives requests */
    ServerSocket workersocket;

    /* Define the socket that is used to handle the connection */
    Socket providerSocket;

    void openServer() {
        try {
            /* Create Server Socket */
            workersocket = new ServerSocket(4321, 10);//the same port as before, 10 connections
            
            while (true) {
                /* Accept the connection */
                providerSocket = workersocket.accept();

                send_work(providerSocket);
            }
 
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void wpt_init() throws IOException, ParserConfigurationException, SAXException
    {
        DocumentBuilderFactory dBfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dBfactory.newDocumentBuilder();
        
		// Fetch GPX File
		Document document = builder.parse(new File(this.gpx));
		document.getDocumentElement().normalize();

        //Get root node - gpx
		Element root = document.getDocumentElement();

        //Get all waypoints
		NodeList nList = document.getElementsByTagName("wpt");

		for (int i = 0; i < nList.getLength(); i++)
		{
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				// Waypoint's elements
				Element element = (Element) node;
                //Init waypoint 
                String user = root.getAttribute("creator");
                Double lon = Double.parseDouble(element.getAttribute("lon"));
                Double lat = Double.parseDouble(element.getAttribute("lat"));
                Double ele = Double.parseDouble(element.getElementsByTagName("ele").item(0).getTextContent());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                LocalDateTime time = LocalDateTime.parse(element.getElementsByTagName("time").item(0).getTextContent(), formatter);
                Waypoint wpt = new Waypoint(user, lon, lat, ele, time);

                wpt_list.add(wpt);
			}
        }
    }

    public void send_work(Socket pro) throws IOException
    {
        try {
            int i = 0;// base iterator
            int index = 0;
            while (index < wpt_list.size())
            {
                Chunk chunk = new Chunk();
                while (i < n) 
                {
                    chunk.add_wpt(wpt_list.get(index));
                    i++;
                    index++;
                }
                i = 0;
                
                /* Handle the request */
                Thread t = new Worker(pro);
                t.start();
            }
        } finally {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    } 
}
