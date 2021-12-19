# [item20] 추상 클래스보다는 인터페이스를 우선하라
자바 8부터 인터페이스도 디폴트 메서드를 제공할 수 있게 되어 추상클래스, 인터페이스 둘 모두 인스턴스 구현 메커니즘을 제공할 수 있다.

## 추상 클래스와 인터페이스의 차이
추상 클래스는 IS-A "~는 ~이다", 인터페이스는 HAS-A "~을 할 수 있다"의 관계다.
즉 추상 클래스를 구현하는 클래스는 반드시 추상 클래스 하위 타입이 되어야 한다. 반면에 **인터페이스는 기존에 클래스에
손쉽게 새로운 인터페이스를 구현해넣을 수 있다.**
## 인터페이스의 유연성
```java
public interface Singer {
    AudioClip sing(Song s);
}
```

```java
public interface Songwriter {
    Song compose(int chartPosition);
}
```

```java
public interface SingerSongwriter extends Singer, Songwriter {
    AudioClip strum();
    void actSensitive();
}
```
만약 이 코드를 클래스로 정의하면 2^n개의 클래스를 만들어야 한다. 흔히 조합 폭발이라 부르는 현상이다.

## 인터페이스와 추상 골격 구현
```java
public interface Car {
    void boot(); // 시동
    void start(); // 출발
    void stop(); // 멈춤

    void charge(); // 충전
}
```

```java
public class Hyundai implements Car {
    @Override
    public void boot() {
        System.out.println("부릉부릉~");
    }

    @Override
    public void start() {
        System.out.println("출발~");
    }

    @Override
    public void stop() {
        System.out.println("멈춰!");
    }

    @Override
    public void charge() {
        System.out.println("현대식 충전~");
    }
}
```

```java
public class Kia implements Car {
    @Override
    public void boot() {
        System.out.println("부릉부릉~");
    }

    @Override
    public void start() {
        System.out.println("출발~");
    }

    @Override
    public void stop() {
        System.out.println("멈춰!");
    }

    @Override
    public void charge() {
        System.out.println("기아식 충전~");
    }
}
```
위 코드는 인터페이스를 사용해서 구현했다. 문제는 charge() 메서드를 제외한 나머지 메서드들이 **중복** 코드다.
이러한 문제를 인터페이스와 추상클래스의 장점을 모두 취하는 방법인 추상 골격 구현으로 해결할 수 있다.
인터페이스로는 타입을 정의하고 추상 클래스로 공통 메서드를 구현하면 된다.
```java
public interface Car {
    void boot(); // 시동
    void start(); // 출발
    void stop(); // 멈춤

    void charge(); // 충전
}
``` 

```java
public abstract class SkeletalCar implements Car {
    @Override
    public void boot() {
        System.out.println("부릉부릉~");
    }

    @Override
    public void start() {
        System.out.println("출발~");
    }

    @Override
    public void stop() {
        System.out.println("멈춰!");
    }
}
``` 

```java
public class Hyundai extends SkeletalCar {
    @Override
    public void charge() {
        System.out.println("현대식 충전~");
    }
}
``` 

```java
public abstract class Kia extends SkeletalCar {
    @Override
    public void charge() {
        System.out.println("기아식 충전~");
    }
}
``` 

## 핵심 정리
- 다중 구현용 타입으로는 인터페이스가 가장 적합하다.
- 복잡한 인터페이스라면 구현하는 수고를 덜어주는 골격 구현을 함께 제공하는 방법을 고려하라.