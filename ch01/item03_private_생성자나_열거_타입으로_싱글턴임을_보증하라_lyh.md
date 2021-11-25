## item_03_private_생성자나_열거_타입으로_싱글턴임을_보증하라_lyh

싱글턴이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말합니다. 싱글턴을 만드는 방법은 생성자를 private로 감춰두고 유일한 인스턴스에 접근할 수 있는 수단으로 public static 멤버를 하나 마련해둡니다. public이나 protected 생성자가 없기 때문에 Elvis 클래스가 초기화될 때 만들어진 인스턴스가 전체 시스템에서 하나뿐임이 보장됩니다.

한 가지 예외는 권한이 있는 사용자가 `AccessibleObject.setAccessible`을 사용해 private 생성자를 호출할 수 있습니다. 이러한 공격을 방어하려면 생성자를 수정해 두 번째 객체가 생성되려 할 때 예외를 던지게 하면 됩니다. 

```java
public class ElvisSingleton {

    private ElvisSingleton() {}

    //멤버 필드로 인스턴스 제공
    public static final ElvisSingleton INSTANCE = new ElvisSingleton();
    //정적 메서드로 인스턴스 제공
    public static ElvisSingleton getInstance() {
        return INSTANCE;
    }
}
```

첫 번째 멤버 필드로 인스턴스를 반환하는 방법의 장점은 멤버 필드의 특성을 이해한다면 해당 클래스가 싱글턴임이 명백히 드러난다는 점입니다. public static에 final로 선언된 필드는 다른 객체를 참조할 수 없습니다.

두 번째 정적 팩터리 방식의 장점은 API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있단 점입니다. 유일한 인스턴스만 반환하던 팩터리 메서드가 호출하는 스레드별로 다른 인스턴스를 만들어 반환해줄 수도 있습니다. 마지막 장점은 정적 팩터리의 메서드 참조를 공급자(supplier)로 사용할 수 있단 점입니다. `Elvis::getInstance`

세 번째 방식은 원소가 하나인 열거 타입을 선언하는 것입니다. 부자연스러워 보이지만 직렬화 상황이나 리플렉션 공격에서도 제2의 인스턴스가 생기는 일을 완벽히 막아줍니다.

```java

public enum ElvisSingletonEnum {

    INSTANCE;

    public void leaveTheBuilding() {
        System.out.println("LEAVE NOW!");
    }
}
```

