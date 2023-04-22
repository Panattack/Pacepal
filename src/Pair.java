import java.io.Serializable;

public class Pair<K, V> implements Serializable{
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
    
    public void setKey(K key) {
        this.key = key;
    }
    
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) { // If the objects are the same reference, they are equal
            return true;
        }

        if (!(obj instanceof Pair)) { // If the object is not an instance of Pair, they are not equal
            return false;
        }

        Pair<?, ?> other = (Pair<?, ?>) obj; // Cast the object to Pair

        // Compare the key and value of the Pair objects
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }

        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }

        return true; // If all comparisons are equal, the objects are equal
    }
}