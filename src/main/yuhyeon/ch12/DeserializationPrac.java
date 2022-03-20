package yuhyeon.ch12;

import java.util.HashSet;
import java.util.Set;

public class DeserializationPrac {

    public static void main(String[] args) {
        deserialize(bomb()); //영원히 지속되는 역직렬화
    }

    static byte[] bomb() {
        Set<Object> root = new HashSet<>();
        Set<Object> s1 = root;
        Set<Object> s2 = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            Set<Object> t1 = new HashSet<>();
            Set<Object> t2 = new HashSet<>();
            t1.add("foo"); //t1를 t2와 다르게 만든다.
            s1.add(t1); s1.add(t2);
            s2.add(t1); s2.add(t2);
            s1 = t1;
            s2 = t2;
        }
        return serialize(root);
    }

    static byte[] serialize(Set<Object> root) {
        //serializing code...
        return new byte[0];
    }

    static Set<Object> deserialize(byte[] input) {
        return new HashSet<Object>();
    }
}
