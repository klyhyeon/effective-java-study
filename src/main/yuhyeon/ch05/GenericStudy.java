package yuhyeon.ch05;

import java.util.*;

public class GenericStudy {

    public static void main(String[] args) {
//        rawTypeUsecase();
        union(Set.of("set1"), Set.of("set2"));
    }

    private static void unsafeAdd(List s1, Object o1) {
        s1.add(o1);
        System.out.println("unsafeAdd!" +  s1.toString());
    }

    private static void genericClassLiteral() {
        Class<String> classLiteral = String.class;
    }

    private static void rawTypeUsecase() {
        Set o = new TreeSet();
        if (o instanceof Collection<?>) {
            Set<?> s = (Set<?>) o;
            System.out.println("cast o to Set<?>");
        }
    }

    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }
}
