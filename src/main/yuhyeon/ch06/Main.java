package yuhyeon.ch06;

import java.util.Arrays;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        Arrays.stream(Planet.values()).forEach(p -> System.out.println(p.toString()));
        System.out.println(Planet.valueOf("JUPITER"));

        int weekdayWorkTime = PayrollDay.MONDAY.pay(500, 2);
        System.out.println(weekdayWorkTime);

        System.out.println("fromString:: ");
        Optional<PayrollDay> day = PayrollDay.fromString("MONDAY");
        int pay = 0;
        if (day.isPresent()) {
            pay = day.get().pay(500, 2);
        }
        System.out.println(pay);
    }
}
