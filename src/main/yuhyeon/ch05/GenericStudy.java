package yuhyeon.ch05;

import java.util.*;

public class GenericStudy {

    public static void main(String[] args) {
//        rawTypeUsecase();
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
}
