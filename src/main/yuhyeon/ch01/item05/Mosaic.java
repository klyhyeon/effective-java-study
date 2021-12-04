package yuhyeon.ch01.item05;

import java.util.function.Supplier;

public class Mosaic {

    private Mosaic(Supplier<? extends Tile> tileFactory) {

    }

    static Mosaic create(Supplier<? extends Tile> tileFactory) {
        tileFactory.get();
        return new Mosaic(tileFactory);
    }
}
