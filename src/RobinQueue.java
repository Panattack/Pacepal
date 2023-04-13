import java.util.LinkedList;

public class RobinQueue<T>{

    private int maxSize;
    private LinkedList<T> queue;
    private int index;

    public RobinQueue (int maxSize) {
        super();
        this.maxSize = maxSize;
        this.index = 0;
        this.queue = new LinkedList<>();
    }

    public void add(T element) {
        this.queue.addLast(element);
    }

    public boolean is_Empty() {
        if (this.queue.size() == 0) {
            return true;
        }
        
        return false;
    }

    public T remove() {
        if (!this.is_Empty()) {
            return this.queue.removeFirst();
        }
        System.out.println("empty");
        return null;
    }

    public T get() {
        if (maxSize == this.index) {
            this.index = 0;
        }
        return this.queue.get(index++);
    }

    public int size() {
        return this.queue.size();
    }
}
