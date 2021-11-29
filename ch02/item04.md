## [item04] 인스턴스화를 막으려거든 private 생성자를 사용하라

정적 메서드와 정적 필드만을 담은 클래스를 만들고 싶을때 private 생성자를 사용하여 인스턴스화를 막아야한다.
예를 들어 java.lang.Math, java.util.Arrays처럼 기본 타입 값이나 배열 관련 메서드들을 모아놓을 수 있다.
또한 java.util.Collections처럼 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메서드(혹은 팩터리)를 모아놓을 수도 있다.
추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다. 하위 클래스를 만들어 인스턴스화하면 그만이다.
private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다.
다음은 java.lang.Math 유틸리티 클래스이다.
adssd

### Math

```java
public final class Math {
    private Math() {}
    public static final double E = 2.7182818284590452354;
    public static final double PI = 3.14159265358979323846;
    private static final double DEGREES_TO_RADIANS = 0.017453292519943295;
    private static final double RADIANS_TO_DEGREES = 57.29577951308232;
}
```

이 처럼 생성자를 private로 선언하여 인스턴스화를 막고 class앞에 final 키워드를 붙여 하위 클래스에 상속도 막아놨다.
즉 해당 클래스는 인스턴스화를 막아놨다.

### 인스턴스를 만들 수 없는 유틸리티 클래스1

```java
public abstract class HelloUtility {
}
```

클래스를 abstact로 만들어 인스턴스화를 막는 방법은 상속을 통해 인스턴스화를 할 수 있다.

### 인스턴스를 만들 수 없는 유틸리티 클래스2

```java
public class HelloUtility {
    private HelloUtility() {
        throw new AssertionError();
    }
}
```

명시적 생성자 private이니 클래스 바깥에서는 접근할 수 없다. 꼭 AssertionError를 던질 필요는 없지만, 클래스 안에서 실수로라도 생성자를 호출하지 않도록 해준다.
