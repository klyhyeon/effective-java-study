## 아이템 5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

클래스가 하나 이상의 내부 자원에 의존적이라면 싱글턴과 정적 유틸리티 클래스는 활용하지 않는 것이 좋습니다. 대신 필요한 자원을 생성자에 넘겨주는 의존 객체 주입 기법을 사용해 클래스의 유연성, 재사용성, 테스트 용이성을 개선시켜 줍니다.

```java
//책 예제코드
public class SpellCheckerInjection {

    private final Lexicon dictionary;

    public SpellCheckerInjection(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
}
/*
        ----------------------------------------------------------
 */
//연습코드
public class Cars {

    private final List<SingleCar> gameCarList;

    //의존 객체 주입방식으로 경기 출전 차량들 등록
    public Cars(List<SingleCar> gameCarList) {
        this.gameCarList = gameCarList;
    }
}
```

의존 객체 주입 패턴의 변형으론 생성자에 자원 팩터리(Supplier<T>)넘겨주는 방식이 있습니다. 팩터리란 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말합니다.

```java

public class Mosaic {
    
    private Mosaic(Supplier<? extends Tile> tileFactory) {
        
    }
    
    Mosaic create(Supplier<? extends Tile> tileFactory) {
        return new Mosaic(tileFactory);    
    }
}
}
```