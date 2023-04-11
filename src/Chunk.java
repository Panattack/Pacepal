import java.io.Serializable;
import java.util.ArrayList;

public class Chunk implements Serializable{
    /* Chunk must implement Serializable in order to pass it in stream */
    private ArrayList<Waypoint> ls_wpt;    
    public int id;
    
    public Chunk(int id)
    {
        this.id = id;
        ls_wpt = new ArrayList<Waypoint>();
    }

    public Chunk(){
        ls_wpt = new ArrayList<Waypoint>();
    }

    public void add(Waypoint wpt)
    {
        this.ls_wpt.add(wpt);
    }

    public int size() {
        return this.ls_wpt.size();
    }

    public Waypoint get(int i) {
        return ls_wpt.get(i);
    }

}
