import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;

public class Dummy {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        MasterWorker master = new MasterWorker("gpxs/gpxs/route1.gpx");
        master.wpt_init();
    }
}
