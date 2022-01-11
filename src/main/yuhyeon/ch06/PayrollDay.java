package yuhyeon.ch06;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static yuhyeon.ch06.PayrollDay.PayType.WEEKDAY;
import static yuhyeon.ch06.PayrollDay.PayType.WEEKEND;

public enum PayrollDay {

//    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
//
//
//    int pay(int minutesWorked, int payRate) {
//        int basePay = minutesWorked * payRate;
//
//        int overtimePay;
//        switch(this) {
//            case SATURDAY: case SUNDAY: //주말
//                overtimePay = basePay / 2;
//                break;
//            default: //주중
//                overtimePay = minutesWorked <= MINS_PER_SHIFT ?
//                        0 : (minutesWorked - MINS_PER_SHIFT) * payRate / 2;
//        }
//
//        return basePay +  overtimePay;
//    }

        //새로운 상수를 추가할 때 잔업수당'전략'을 선택하도록 하는 것입니다.
        //PayType을 추가해 중첩 열거타입으로 옮기고 PayrollDay 생성자에서 적당한 것을 선택합니다.
        //PayrollDay는 잔업수당 계산을 전략 열거 타입(PayType)에 위임하여 상수별 메서드 구현이 필요없게 됩니다.
        MONDAY(WEEKDAY),
        TUESDAY(WEEKDAY),
        WEDNESDAY(WEEKDAY),
        THURSDAY(WEEKDAY),
        FRIDAY(WEEKDAY),
        SATURDAY(WEEKEND),
        SUNDAY(WEEKEND);

        private static final int MINS_PER_SHIFT = 8 * 60;
        private final PayType payType;

        PayrollDay(PayType payType) {
                this.payType = payType;
        }

        int pay(int minutesWorked, int payRate) {
                return payType.pay(minutesWorked, payRate);
        }

        enum PayType {
                WEEKDAY {
                        int overtimePay(int minsWokred, int payRate) {
                                return minsWokred <= MINS_PER_SHIFT ? 0 :
                                        (minsWokred - MINS_PER_SHIFT) * payRate / 2;
                        }
                },
                WEEKEND {
                        int overtimePay(int minsWorked, int payRate) {
                                return minsWorked * payRate / 2;
                        }
                };


                abstract int overtimePay(int mnins, int payRate);

                int pay(int minsWorked, int payRate) {
                        int basePay = minsWorked * payRate;
                        return basePay +  overtimePay(minsWorked, payRate);
                }
        }

        private static final Map<String, PayrollDay> stringToEnum =
                Stream.of(values())
                        .collect(Collectors.toMap(Objects::toString, a -> a));

        public static Optional<PayrollDay> fromString(String symbol) {
                return Optional.ofNullable(stringToEnum.get(symbol));
        }

}
