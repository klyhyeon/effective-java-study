package yuhyeon.ch06.enummap;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class Main {

    public static void main(String[] args) {
        Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
                new EnumMap<>(Plant.LifeCycle.class);

        Arrays.stream(Plant.LifeCycle.values())
                .forEach(lc -> plantsByLifeCycle.put(lc, new HashSet<>()));

        List<Plant> garden = new ArrayList<>();
        garden.add(new Plant("rose", Plant.LifeCycle.ANNUAL));
        garden.add(new Plant("camomile", Plant.LifeCycle.BIENNIAL));

        garden.forEach(p -> plantsByLifeCycle.get(p.lifeCycle).add(p));
        System.out.println(plantsByLifeCycle);
        //{ANNUAL=[Plant{name='rose', lifeCycle=ANNUAL}], PERENNIAL=[], BIENNIAL=[Plant{name='camomile', lifeCycle=BIENNIAL}]}
        System.out.println(garden.stream()
                .collect(Collectors.groupingBy(p -> p.lifeCycle,
                        () -> new EnumMap<>(Plant.LifeCycle.class),
                        toSet())));
        //{ANNUAL=[Plant{name='rose', lifeCycle=ANNUAL}], BIENNIAL=[Plant{name='camomile', lifeCycle=BIENNIAL}]}

    }
}
