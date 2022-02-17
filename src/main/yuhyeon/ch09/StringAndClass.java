package yuhyeon.ch09;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StringAndClass {

    private void badExamleUsingString() {
        List<Integer> list = List.of(1,2,3);
        Iterator i = list.iterator();
        String className = "class1";
        String compoundKey = className + "#" + i.next();
    }

    public static class ThreadLocal {
        private ThreadLocal() {

        }

        //현 스레드의 값을 키로 구분해 저장
        public static void set(String key, Object value) {

        }

        // (키가 가리키는) 현 스레드의 값을 반환
        public static Object get(String key) {
            return null;
        }
    }

    public static class ThreadLocal2 {

        private ThreadLocal2() {

        }

        public static class Key {
            Key() {

            }
        }

        //위조 불가능한 고유의 키 생성
        public static Key getKey() {
            return new Key();
        }

        public void set(Key key, Object value) {

        }

        public Object get(Key key) {
            return null;
        }

    }

    //Key를 ThreadLocal3로 번경
    public final class ThreadLocal3 {
        public ThreadLocal3() {}
        public void set(Object value) {

        }
        public Object get() {
            return null;
        }
    }

    public final class ThreadLocal4<T> {
        public ThreadLocal4() {}
        public void set(T value) {

        }
        public T get() {
            return null;
        }
    }

    public static void main(String[] args) {
    }
}
