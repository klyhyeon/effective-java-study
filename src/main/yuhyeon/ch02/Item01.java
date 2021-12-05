package yuhyeon.ch02;

import java.util.EnumSet;

public class Item01 {

    enum Nasdaq { AAPL, MSFT, TSLA, FB, AMZN};

    public static void main(String[] args) {

        EnumSet<Nasdaq> set1, set2, set3, set4;
        set1 = EnumSet.of(Nasdaq.AAPL, Nasdaq.MSFT, Nasdaq.TSLA, Nasdaq.FB);
        set2 = EnumSet.complementOf(set1);
        set3 = EnumSet.allOf(Nasdaq.class);
        set4 = EnumSet.range(Nasdaq.MSFT, Nasdaq.FB);

        System.out.println("Set 1 : " +  set1);
        System.out.println("Set 2 : " +  set2);
        System.out.println("Set 3 : " +  set3);
        System.out.println("Set 4 : " +  set4);
    }


}
