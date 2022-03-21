# [item89]. 인스턴스 수를 통제해야 한다면 readResolve 보다는 열거 타입을 사용하라

## 싱글턴 패턴, Serializable
```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}

    public void leaveTheBuilding() {}
}
```
이 클래스 선언에 implements Serializable을 추가하는 순간 더 이상 싱글턴이 아니게 된다. 기본 직렬화를 쓰지 않더라도 그리고 명시적인 readObject를 제공하더라도 소용없다. 어떤 readObject를 사용하든 이 클래스가 초기화될 때 만들어진 인스턴스와는 별개인 인스턴스를 반환하게 된다.

## readResolve()
readResolve() 메서드를 이용하면 readObject가 만들어낸 인스턴스를 다른것으로 대체할 수 있따. 역직렬화한 객체의 클래스가 readResolve메서드를 적절히 정의해뒀다면, 역직렬화한 후 새로 생성된 객체를 인수로 이 메서드가 호출되고, 이 메서드가 반환한 객체 참조가 새로 생성된 객체를 대신해 반환된다.

1. 역직렬화한 새로 성생된 객체를 인수로 readResolve() 호출
2. 메서드가 반환한 객체를 새로 생성한 객체를 대신해 반환
3. 새로 성성된 객체는 참조를 유지하지 않으므로 GC의 대상이 된다.

## readResolve()를 활용한 싱글턴 보장
```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}

    public void leaveTheBuilding() {}

    private Object readResolve() {
        return INSTANCE;
    }
}
```

## Serializable을 이용한 공격
이 메서드는 역직렬화한 객체는 무시하고 클래스 초기화 때 만들어진 Elvis 인스턴스를 반환한다. 따라서 Elvis 인스턴스의 직렬화 형태는 아무런 실 데이터를 가질 이유가 없으니 모든 인스턴스 필드를 transient로 선언해야 한다. **사실, readResolve를 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드는 모두 transient로 선언해야 한다**

공격 기본 아이디어는 간단하다. 싱글턴이 transient가 아닌 참조 필드를 가지고 있다면, 그 필드의 내용은 readResolve 메서드가 실행되기 전에 역 직렬화된다. 그렇다면 잘 조작된 스트림을 써서 해당 참조 필드의 내용이 역 직렬화되는 시점에 그 역직렬화된 인스턴스의 참조를 훔쳐올 수 있다.

```java
public class Elvis implements Serializable {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}

    private String[] favoriteSongs = { "Hound Dog", "HeartBreak Hotel"};
    public void printFavorite() {
        System.out.println(Arrays.toString(favoriteSongs));
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
```

```java
public class ElvisStealer implements Serializable {

    static Elvis impersonator;
    private Elvis payload;

    private Object readResolve() {
        // resolve 되기 전의 Elvis 인스턴스의 참조를 저장한다.
        impersonator = payload;
        // favoriteSongs 필드에 맞는 타입의 객체를 반환한다.
        return new String[] { "A Fool Such as I" };
    }

    private static final long serialVersionUID = 0;

}
```

```java
public class ElvisImpersonator {
    private static final byte[] serializedForm = new byte[] { (byte) 0xac,
            (byte) 0xed, 0x00, 0x05, 0x73, 0x72, 0x00, 0x05, 0x45, 0x6c, 0x76,
            0x69, 0x73, (byte) 0x84, (byte) 0xe6, (byte) 0x93, 0x33,
            (byte) 0xc3, (byte) 0xf4, (byte) 0x8b, 0x32, 0x02, 0x00, 0x01,
            0x4c, 0x00, 0x0d, 0x66, 0x61, 0x76, 0x6f, 0x72, 0x69, 0x74, 0x65,
            0x53, 0x6f, 0x6e, 0x67, 0x73, 0x74, 0x00, 0x12, 0x4c, 0x6a, 0x61,
            0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x4f, 0x62, 0x6a,
            0x65, 0x63, 0x74, 0x3b, 0x78, 0x70, 0x73, 0x72, 0x00, 0x0c, 0x45,
            0x6c, 0x76, 0x69, 0x73, 0x53, 0x74, 0x65, 0x61, 0x6c, 0x65, 0x72,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x01,
            0x4c, 0x00, 0x07, 0x70, 0x61, 0x79, 0x6c, 0x6f, 0x61, 0x64, 0x74,
            0x00, 0x07, 0x4c, 0x45, 0x6c, 0x76, 0x69, 0x73, 0x3b, 0x78, 0x70,
            0x71, 0x00, 0x7e, 0x00, 0x02 };

    public static void main(String[] args) {
        Elvis elvis = (Elvis) deserialize(serializedForm);
        Elvis impersonator = ElvisStealer.impersonator;

        elvis.printFavorite();
        impersonator.printFavorite();
    }

    private static Object deserialize(byte[] sf) {
        try {
            InputStream is = new ByteArrayInputStream(sf);
            ObjectInputStream ois = new ObjectInputStream(is);
            return ois.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
```

## 열거 타입 싱글턴
```java
public enum Elvis {
    INSTANCE;

    private String[] favoriteSongs = { "Hound Dog", "HeartBreak Hotel"};
    public void printFavorite() {
        System.out.println(Arrays.toString(favoriteSongs));
    }
}
```