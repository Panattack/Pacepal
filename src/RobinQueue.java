import java.util.LinkedList;

public class RobinQueue<T>{

    private int maxSize;
    private LinkedList<T> queue;
    private int index;

    public RobinQueue (int maxSize) {
        this.maxSize = maxSize;
        this.index = 0;
        this.queue = new LinkedList<>();
    }

    public synchronized int getIndex()
    {
        return this.index;
    }

    public synchronized void add(T element) {
        this.queue.addLast(element);
    }

    public synchronized boolean is_Empty() {
        if (this.queue.size() == 0) {
            return true;
        }
        
        return false;
    }

    public synchronized T get() {
        T o = queue.get(this.index % maxSize);
        this.index++;
        return o;
    }

    public synchronized int size() {
        return this.queue.size();
    }
}
