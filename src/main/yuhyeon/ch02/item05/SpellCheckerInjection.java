package yuhyeon.ch02.item05;

import java.util.Objects;

public class SpellCheckerInjection {

    private final Lexicon dictionary;

    public SpellCheckerInjection(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

}
