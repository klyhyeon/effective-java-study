package yuhyeon.ch08;

import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Date start = new Date();
        Date end = new Date();
        Period p = new Period(start, end);
        p.getEnd().setTime(0000L); //Date의 setTime을 통해 여전히 장난칠 수 있습니다.
    }
}
