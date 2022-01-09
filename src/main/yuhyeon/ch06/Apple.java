package yuhyeon.ch06;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Apple {

    FUJI,
    PIPIIN,
    GRANNY_SMITH;

    private static final Map<String, Apple> stringToEnum =
            Stream.of(values())
                    .collect(Collectors.toMap(Objects::toString, a -> a));

    public static Optional<Apple> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }
}
