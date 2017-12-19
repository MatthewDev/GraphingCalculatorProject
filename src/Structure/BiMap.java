package Structure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matthew on 11/13/2017.
 */
public class BiMap<K, V> implements Map<K, V>{
    HashMap<K, V> KV = new HashMap<>();
    HashMap<V, K> VK = new HashMap<>();

    @Override
    public int size() {
        return KV.size();
    }

    @Override
    public boolean isEmpty() {
        return KV.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return KV.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return KV.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return KV.get(key);
    }

    public K getKey(V value) {
        return VK.get(value);
    }

    @Override
    public V put(K key, V value) {
        VK.put(value, key);
        return KV.put(key, value);
    }

    @Override
    public V remove(Object key) {
        if(KV.containsKey(key)) {
            V value = KV.get(key);
            VK.remove(value);
        }
        return KV.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Entry<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            put(key, value);
        }
    }

    @Override
    public void clear() {
        KV.clear();
        VK.clear();
    }

    @Override
    public Set<K> keySet() {
        return KV.keySet();
    }

    @Override
    public Collection<V> values() {
        return KV.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return KV.entrySet();
    }
}
