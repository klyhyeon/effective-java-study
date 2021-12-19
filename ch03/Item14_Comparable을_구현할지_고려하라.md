- compareTo : 동치성 비교 + **순서 비교**
- Comparable을 구현했다는 것은 그 클래스의 인스턴스들에는 자연적인 순서가 있음을 뜻한다.
- String은 Comparable을 구현한다.

```java
package com.practice.effective_java.item14;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class WordList {

    public static void main(String[] args) {
        Set<String> s = new TreeSet<>();
        Collections.addAll(s, "cat", "bird", "apple", "tree", "set");
        System.out.println(s);
        // [apple, bird, cat, set, tree]
    }
}
```

- 사실상 자바 플랫폼 라이브러리의 모든 값 클래스와 열거 타입이 Comparable을 구현했다.
- **알파벳, 숫자, 연대 같이 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable 인터페이스를 구현하자.**

### compareTo의 일반 규약

- 이 객체와 주어진 객체의 순서를 비교한다. 이 객체가 주어진 객체보다 작으면 음의 정수를, 같으면 0을, 크면 양의 정수를 반환한다. 이 객체와 비교할 수 없는 타입의 객체가 주어지면 ClassCastException을 던진다.
1. 두 객체 참조의 순서를 바꿔 비교해도 예상한 결과가 나와야 한다.
2. 추이성을 보장해야 한다. 즉 (x.compareTo(y) > 0 && y.compareTo(z) > 0)이면 x.compareTo(z) > 0이다.  (x.compareTo(y) == 0 && y.compareTo(z) == 0)일 때도 x.compareTo(z) == 0이어야 한다.
3. 크기가 같은 객체들끼리는 어떤 객체와 비교하더라도 항상 같아야 한다.
4. (권고) (x.compareTo(y) == 0) == (x.equals(y))여야 한다. 즉 compareTo 메서드로 수행한 동치성 테스트의 결과가 equals와 같아야 한다.
- 기존 클래스를 확장한 구체 클래스에서 새로운 값 컴포넌트를 추가했다면 compareTo 규약을 지킬 방법이 없다.

- 정렬된 컬렉션은 동치성을 비교할 때 equals가 아닌 compareTo를 사용한다.

```java
package com.practice.effective_java.item14;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.TreeSet;

public class BigDecimalTest {

    public static void main(String[] args) {

        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("1.00");

        System.out.println(a.equals(b));    // false
        System.out.println(a.compareTo(b)); // 0

        HashSet<BigDecimal> set1 = new HashSet<>();
        set1.add(a);
        set1.add(b);
        System.out.println(set1);    // [1.0, 1.00]
        // equals 메서드로 비교하면 서로 다르기 때문에 원소 2개

        TreeSet<BigDecimal> set2 = new TreeSet<>();
        set2.add(a);
        set2.add(b);
        System.out.println(set2);   // [1.0]
        // 정렬된 컬렉션들은 동치성을 비교할 떄 equals 대신에 compareTo를 사용
        // compareTo로 비교하면 두 인스턴스가 똑같기 때문에 원소 1개만 출력

    }
}
```

### Comparable 구현 / Comparator 생성

```java
package com.practice.effective_java.item14;

import java.util.Collections;
import java.util.TreeSet;

final class CaseIntensiveString implements Comparable<CaseIntensiveString> {

    private String str;

    public CaseIntensiveString(String str) {
        this.str = str;
    }

    @Override
    public int compareTo(CaseIntensiveString o) {
        return String.CASE_INSENSITIVE_ORDER.compare(str, o.str);
    }

    @Override
    public String toString() {
        return str;
    }
}

public class CaseIntensiveStringTest {

    public static void main(String[] args) {

        CaseIntensiveString s1 = new CaseIntensiveString("cat");
        CaseIntensiveString s2 = new CaseIntensiveString("Bus");
        CaseIntensiveString s3 = new CaseIntensiveString("Apple");

        TreeSet<CaseIntensiveString> set = new TreeSet<>();
        Collections.addAll(set, s1, s2, s3);
        System.out.println(set);
    }
}
```

- 가장 핵심적인 필드부터 비교하자.
- 박싱된 기본 타입 클래스들에 새로 추가된 정적 메서드인 compare를 이용하자.
- 자바 8부터 메서드 연쇄 방식으로 비교자를 생성할 수 있게 되었으나 약간의 성능 저하가 뒤따른다.

```java
package com.practice.effective_java.item14;

import java.util.Comparator;

class PhoneNumber implements Comparable<PhoneNumber> {

    private Short areaCode;
    private Short prefix;
    private Short lineNum;

    public PhoneNumber(Short areaCode, Short prefix, Short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

//    private static final Comparator<PhoneNumber> COMPARATOR = new Comparator<PhoneNumber>() {
//        @Override
//        public int compare(PhoneNumber o1, PhoneNumber o2) {
//            int result = Short.compare(o1.areaCode, o2.areaCode);
//            if (result == 0) {
//                result = Short.compare(o1.prefix, o2.prefix);
//                if (result == 0) {
//                    result = Short.compare(o1.lineNum, o2.lineNum);
//                }
//            }
//            return result;
//        }
//    };

    private static final Comparator<PhoneNumber> COMPARATOR
            // 람다를 인수로 받는다
            // 입력 인수의 타입 (PhoneNumber o)를 명시
            = Comparator.comparingInt((PhoneNumber o) -> o.areaCode)
            .thenComparingInt(o -> o.prefix)
            .thenComparingInt(o -> o.lineNum);

    @Override
    public int compareTo(PhoneNumber o) {
        return COMPARATOR.compare(this, o);
/*

        // #1
        // 실수 기본 타입 필드, 정수 기본 타입 필드 둘 다 박싱된 기본 타입 클래스들에 추가된 정적 메소드 활용해서 비교
        int result = Short.compare(areaCode, o.areaCode);
        if (result == 0) {
            result = Short.compare(prefix, o.prefix);
            if (result == 0) {
                result = Short.compare(lineNum, o.lineNum);
            }
        }
        return result;
*/

    }
}

public class ChainingComparatorTest {

}
```

- 객체 참조용 비교자 생성 메서드
- 먼저 키 추출자를 받아서 그 키의 자연적 순서를 사용하고, thenComparing으로 부차 순서를 정한다.

```java
package com.practice.effective_java.item14;

import java.util.Comparator;

class Sample implements Comparable<Sample> {

    String s1;
    String s2;

    public Sample(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public int compareTo(Sample o) {
        return String.CASE_INSENSITIVE_ORDER.compare(s1, o.s1);
    }
}

public class ComparingTest {

    private static final Comparator<Sample> COMPARATOR =
            Comparator.comparing((Sample s) -> s)
            .thenComparing(s -> s.s2);

    public static void main(String[] args) {

        Sample s1 = new Sample("apple", "dog");
        Sample s2 = new Sample("apple", "case");

        System.out.println(s1.compareTo(s2));               // 0
        System.out.println(COMPARATOR.compare(s1, s2));     // 1

    }
}
```
