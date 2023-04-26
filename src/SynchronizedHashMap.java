import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SynchronizedHashMap<K, V> {

    private final Map<K, V> map;

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        // Constructor
        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        // Getter for key
        @Override
        public K getKey() {
            return key;
        }

        // Getter for value
        @Override
        public V getValue() {
            return value;
        }

        // Setter for value
        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

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

    public Set<Map.Entry<K, V>> entrySet() {
        // Create a new HashSet to store the entries
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        final Node<K, V>[] table = new Node[map.size()]; // Array of Nodes to store buckets
        // Iterate through the buckets (array of linked nodes)
        for (Node<K, V> node : table) {
            while (node != null) {
                // Add each entry to the entrySet
                entrySet.add(node);
                node = node.next;
            }
        }
        // Return the entrySet
        return entrySet;
    }
}