package yuhyeon.ch06.enummap;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class Main {

    public static void main(String[] args) {
        //좋지 않은 코드 예 - ordinal 사용
        Set<Plant>[] plantsByLifeCycleBadExample =
                (Set<Plant>[]) new Set[Plant.LifeCycle.values().length]; //비검사 형변환 수행
        for (int i = 0; i < plantsByLifeCycleBadExample.length; i++) {
            plantsByLifeCycleBadExample[i] = new HashSet<>();
        }

        List<Plant> gardenBadExample = new ArrayList<>();
        for (Plant p : gardenBadExample) {
            plantsByLifeCycleBadExample[p.lifeCycle.ordinal()].add(p); //ordinal을 배열 인덱스로 사용
        }


        //좋은 코드 예 - EnumMap 사용
        Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
                new EnumMap<>(Plant.LifeCycle.class); //키 타입의 Class 객체, 런타임 제네익 타입 정보를 제공

        Arrays.stream(Plant.LifeCycle.values())
                .forEach(lc -> plantsByLifeCycle.put(lc, new HashSet<>()));

        List<Plant> garden = new ArrayList<>();
        garden.add(new Plant("rose", Plant.LifeCycle.ANNUAL));
        garden.add(new Plant("camomile", Plant.LifeCycle.BIENNIAL));

        garden.forEach(p -> plantsByLifeCycle.get(p.lifeCycle)
                .add(p));
        System.out.println(plantsByLifeCycle);
        //{ANNUAL=[Plant{name='rose', lifeCycle=ANNUAL}], PERENNIAL=[], BIENNIAL=[Plant{name='camomile', lifeCycle=BIENNIAL}]}

        //stream을 사용하면 속하는 열거타입이 있을 때에만 중첩 맵을 만듭니다.
        System.out.println(garden.stream()
                .collect(Collectors.groupingBy(
                        p -> p.lifeCycle,
                        () -> new EnumMap<>(Plant.LifeCycle.class),
                        toSet())));
        //{ANNUAL=[Plant{name='rose', lifeCycle=ANNUAL}], BIENNIAL=[Plant{name='camomile', lifeCycle=BIENNIAL}]}
    }
}
