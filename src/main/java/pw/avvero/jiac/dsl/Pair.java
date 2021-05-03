package pw.avvero.jiac.dsl;

public class Pair<K, V> {

    private K key;
    private V v;

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }

    public Pair(K key, V v) {
        this.key = key;
        this.v = v;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "{" +
                "key=" + key +
                ", v=" + v +
                '}';
    }
}
