package version_1;

import java.io.Serializable;

/**
 * Custom class created to easily send packages through the socket
 * @param <T> first object
 * @param <V> second object
 */
public class Pair<T, V> implements Serializable {
    private T first;
    private V second;

    public Pair(T first, V second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public boolean equals(Pair<T, V> pair){
        if(pair.getFirst().equals(this.first) && pair.getSecond().equals(this.second))
            return true;
        return false;
    }

    public void print(){
        System.out.println(first + " - "  + second);
    }
}
