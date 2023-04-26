import java.util.HashMap;
import java.util.Map;

public class SynchronizedHashMap<K, V> {
    private final Map<K, V> map;

    public SynchronizedHashMap() {
        this.map = new HashMap<>();
    }

    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized void remove(K key) {
        map.remove(key);
    }

    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public synchronized boolean containsValue(V value) {
        return map.containsValue(value);
    }

    public synchronized int size() {
        return map.size();
    }

    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    public synchronized void clear() {
        map.clear();
    }
}