# Item06 불필요한 객체 생성을 피하라 
## 예제 1 - String 인스턴스 생성
```java
        String a = "hello";
        String b = "hello";          //String Constant Pool & String literal
        System.out.println(a == b); //동일성 비교 (인스턴스 주소값 비교)
        System.out.println(a.equals(b));    //동질성 비교 (객체 내부 값 비교)
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());

        //스트링 선언 2: 새로운 인스턴스 생성, 메모리 할당 
        String newA = new String("hello");
        String newB = new String("hello");
        System.out.println(newA == newB);
        System.out.println(newA.equals(newB));
```

```
//출력값
true
true
99162322
99162322
false
true
```


> **String Pool이란?** <br><br>
Java Heap Memory 내에 문자열 리터럴을 저장한 공간.(HashMap으로 구현)
한번 생성된 문자열 리터럴은 변경될 수 없다.
문자열 리터럴은 클래스가 메모리에 로드될 때 자동적으로 미리 생성된다.
리터럴로 문자열을 생성하면(내부적으로 String.intern() 호출)


- Constant String Pool에 같은 값이 있는지 찾는다.<br>
- 같은 값이 있으면 그 참조값이 반환된다.<br>
- 같은 값이 없으면 String Pool에 문자열이 등록된 후 해당 참조값이 반환된다.<br>
- new String("") 의 경우에는 스트링 풀 사용없이 새로운 인스턴스가 매번 생성된다.<br>

## Boolean형은 new Boolean()보다  Boolean.valueOf()를 사용하라

```java
    public static Boolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);

    public static boolean parseBoolean(String s) {
    return "true".equalsIgnoreCase(s);
    }
```

+stackOverflow  example

- worst 
```java
boolean value = new Boolean("true").booleanValue();
boolean value = new Boolean("true")
```
Boolean Object 생성 

- better
```java
boolean value = Boolean.valueOf("true");
```
불필요한 객체 생성을 막을 수 있는 방법. 책에서 설명하는 대안과 동일함

- much better
```java
boolean value = Boolean.parseBoolean("true");
```
- 반환 타입을 기본 타입으로 할 수 있는 최적의 방법으로 설명함 

### 기본타입과 wrapper 클래스, 언박싱
![이미지1](https://miro.medium.com/max/1400/0*WAqW9RS_fVqZ_M1B.jpg)
![이미지2](https://sangwoo0727.github.io/assets/img/java/202003311.png)

```java
    long sum(){
        Long sum = 0L;
        for (long i = 0 ; i<= Integer.MAX_VALUE; i++)
            sum+= i;
        return sum;
    }
```

좋지 못한 예로, Long -> long 언박싱이 개입됨 


### 생성비용이 비싼 객체가 반복적으로 사용된다면 캐싱하여 재사용하자

```java
    static boolean isRomanNumeralSlow(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
                + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    }
```

```java
public class RomanNumerals {

    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})"
                    + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }

}
```

- String.matcher는 내부에서 정규표현식용 Patterns를 만드는데, 한번 쓰고 버려져 바로 가비지 컬렉션 대상이 된다.
- 입력받은 정규표현식에 해당하는 유한 상태머신(finite state machine)을 만들기 때문에 인스턴스 생성 비용이 높다.
- 성능 개선을 위해 불변의 pattern 인스턴스를 클래스 초기화 과정에서 직접 생성해 캐싱해두고, 나중에 isRomanNumeral 메서드 호출시마다 이 인스턴스를 재활용하는 것이 바람직하다.



### 참조
[참조1](https://jaehun2841.github.io/2019/01/07/effective-java-item6/#%EC%95%84%EC%A3%BC-%EC%95%88-%EC%A2%8B%EC%9D%80-%EA%B0%9D%EC%B2%B4-%EC%83%9D%EC%84%B1%EC%9D%98-%EC%98%88)
[참조2](https://stackoverflow.com/questions/12044721/best-performance-for-string-to-boolean-conversion)



