package yuhyeon.ch06;

import java.util.Set;

public class Text {
    public enum Style { BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }

    public void applyStyles(Set<Style> styles) {
        //...
        Set<Text>[] textByLifeCycle =
                (Set<Text>[]) new Set[2];
        //{Set<Text>1, Set<Text>2,....
    }
}
