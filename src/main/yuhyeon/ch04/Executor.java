package yuhyeon.ch04;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Executor {

    public static void main(String[] args) {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(List.of("틱", "탁탁", "펑"));
        System.out.println("unfixed::: " + s.getAddCount());

        /* 래퍼 클래스와 전달 메서드를 사용한 컴포지션 방식*/
        Set<String> set = new TreeSet<>();
        set.add("틱");
        set.add("탁탁");
        set.add("펑");
        FixedInstrumentedHashSet<String> fs = new FixedInstrumentedHashSet<>(set);
        fs.addAll(set);
        System.out.println("fixed::: " + fs.getAddCount());
    }
}
