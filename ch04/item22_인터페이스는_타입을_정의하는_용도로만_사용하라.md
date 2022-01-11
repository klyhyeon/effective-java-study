# Item22 인터페이스는 타입을 정의하는 용도로만 사용하라

## 상수(Static Final)와 인터페이스
- 인터페이스는 자신을 구현한 클래스의 인스턴스를 참조하는 타입 역할을 한다.
- 자신의 인스턴스로 무엇을 할 수 있는지 클라이언트에게 이야기 하는 용도로만 사용해야한다.
- 이장에서는 상수 인터페이스를 Anti Pattern(사용을 지양해야하는 패턴)으로 소개하고, 단점과 보완방법에 대해 설명한다.
  
  코드 22-1

  ```java
  package effectivejava.chapter4.item22.constantinterface;
    public interface PhysicalConstants {
    // 아보가드로 수 (1/몰)
    static final double AVOGADROS_NUMBER   = 6.022_140_857e23;

    // 볼츠만 상수 (J/K)
    static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;

    // 전자 질량 (kg)
    static final double ELECTRON_MASS      = 9.109_383_56e-31;
    }

    //제어자 생략 가능-생략된 제어자는 컴파일 시 자바 컴파일러가 자동으로 추가해 줍니다.
  ```

<br>

  ## 상수 인터페이스 정의와 사용상의 편의
  - 메소드없이 상수만으로 정의된 인터페이스를 말한다.
  - 상수처럼 어디에서나 접근할 수 있다. 
  - 하나의 클래스에 여러 개의 인터페이스를 Implement 할 수 있어  Constant Interface를 Implement 할 경우, ​인터페이스의 클래스 명을 네임스페이스로 붙이지 않고 바로 사용할 수 있다. 이러한 편리성 때문에 Constant Interface를 사용한다.

## 상수 인터페이스가 안티패턴인 이유
- 클래스 내부에서 사용하는 상수는 외부 인터페이스가 아니라 내부 구현에 해당하므로, 내부 구현을 클래스의 API로 노출하는 행위에 해당한다.
- 클라이언트에게는 상수가 의미 없으며, 클라이언트 코드에 혼란을 줄 수 있다.
- 사용이 필요없는 상수 전체를 가지고 온다
- Binary Code Compatibility (이진 호환성)을 필요로 하는 프로그램일 경우, 새로운 라이브러리를 연결하더라도, 상수 인터페이스는 프로그램이 종료되기 전까지 이진 호환성을 보장하기 위해 계속 유지되어야 한다.
- IDE가 없으면, 상수 인터페이스를 Implement 한 클래스에서는 상수를 사용할 때 네임스페이스를 사용하지 않으므로, 해당 상수의 출처를 쉽게 알 수 없다. **또한 상수 인터페이스를 구현한 클래스의 하위 클래스들의 네임스페이스도 인터페이스의 상수들로 오염된다.**

## 대체 방법
1. 특정 클래스나 인터페이스와 연관이 짙다면 해당 클래스나 인스턴스에 포함시켜 준다. 예: Integer 박싱 클래스 
```java
public final class Integer extends Number
        implements Comparable<Integer>, Constable, ConstantDesc {
    /**
     * A constant holding the minimum value an {@code int} can
     * have, -2<sup>31</sup>.
     */
    @Native public static final int   MIN_VALUE = 0x80000000;

    /**
     * A constant holding the maximum value an {@code int} can
     * have, 2<sup>31</sup>-1.
     */
    @Native public static final int   MAX_VALUE = 0x7fffffff;
    }
```
2. 열거형 클래스를 사용합니다.


3. 인스턴스화 할 수 없는 유틸리티 클래스를 사용한다.

```java
package effectivejava.chapter4.item22.constantutilityclass;

// 코드 22-2 상수 유틸리티 클래스 (140쪽)
public class PhysicalConstants {
  private PhysicalConstants() { }  // 인스턴스화 방지

  // 아보가드로 수 (1/몰)
  public static final double AVOGADROS_NUMBER = 6.022_140_857e23;

  // 볼츠만 상수 (J/K)
  public static final double BOLTZMANN_CONST  = 1.380_648_52e-23;

  // 전자 질량 (kg)
  public static final double ELECTRON_MASS    = 9.109_383_56e-31;
}
```

3-1. 정적 임포트를 사용하여 이름을 생략하여 사용할 수 있습니다.

```java
import static effectivejava.chapter4.item22.constantutilityclass.PhysicalConstants.*;

public class Test {
    double atoms(double mols){
        return AVOGADROS_NUMBER * mols;
    }

}
```
