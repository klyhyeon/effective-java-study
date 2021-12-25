package yuhyeon.ch05;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GenericStudy {

    public static void main(String[] args) {
        rawTypeUsecase();

        List<String> list1 = new ArrayList<>();
        Integer i = 1;
        unsafeAdd(list1, i);
        String s = list1.get(0);
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
        if (o instanceof Set<String>) {
            Set<?> s = (Set<?>) o;
            System.out.println("cast o to Set<?>");
        }
    }
}
