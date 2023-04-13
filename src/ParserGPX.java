import java.io.*;
import java.net.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ParserGPX {

    public ArrayList<Waypoint> parse(File file)
    {
        ArrayList<Waypoint> wpt_list = new ArrayList<>();
        try {
            DocumentBuilderFactory dBfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dBfactory.newDocumentBuilder();
            
            // Fetch GPX File
            Document document = builder.parse(file);
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
                    //System.out.println("User_id: " + user + " " + " Wpt_id: " + i);
                }
            }
        }
        catch (ParserConfigurationException p)
        {
            p.printStackTrace();
        }
        catch (SAXException s)
        {
            s.printStackTrace();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
        return wpt_list;
    }
}
