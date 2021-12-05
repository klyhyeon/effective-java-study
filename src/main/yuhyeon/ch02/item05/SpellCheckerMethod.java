package yuhyeon.ch02.item05;

public class SpellCheckerMethod {

    public static final Lexicon dictionary = new Lexicon();

    private SpellCheckerMethod() {}
    public static final SpellCheckerMethod INSTANCE = new SpellCheckerMethod();

    public static SpellCheckerMethod getInstance() {
        return INSTANCE;
    }

}
