package yuhyeon.ch06.enummap;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum Phase {

    SOLID, LIQUID, GAS, PLASMA;

    public enum Transition {

        MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID),
        IONIZE(GAS, PLASMA), DEIONIZE(PLASMA, GAS);

        private Phase from;
        private Phase to;

        Transition(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }

        //행은 from의 ordinal을, 열은 to의 ordinal을 인덱스로 쓴다.
        private static final Transition[][] TRANSITIONS = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };

        //상전이 맵을 초기화한다.
        private static final Map<Phase, Map<Phase, Transition>>
                m = Arrays.stream(values()).collect(
                /**
                 * groupingBy(classifier, mapfactory)
                 * classifier(key) – a classifier function mapping input elements to keys
                 * mapFactory(class?, 구현 인스턴스) – a supplier providing a new empty Map into which the results will be inserted
                 * downstream(value) – a Collector implementing the downstream reduction
                 */
                //key
                Collectors.groupingBy(
                        t -> t.from,
                        () -> new EnumMap<>(Phase.class),
                        /**
                         * toMap(keyMapper, valueMapper)
                         * keyMapper – a mapping function to produce keys
                         * valueMapper – a mapping function to produce values
                         * mergeFunction – a merge function, used to resolve collisions between values associated with the same key, as supplied to Map.merge
                         */
                        //value
                        Collectors.toMap(
                                t -> t.to,
                                t -> t,
                                (x, y) -> y, () -> new EnumMap<>(Phase.class)
                        )
                )
        );

        //한 상태에서 다른 상태로의 전이를 반환한다.
        public static Transition from(Phase from, Phase to) {
            //ordinal은 수정에 취약하다.
//            return TRANSITIONS[from.ordinal()][to.ordinal()];
            return m.get(from).get(to);
        }
    }//end Transition

}
