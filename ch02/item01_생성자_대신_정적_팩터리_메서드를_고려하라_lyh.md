## 아이템 1. 생성자 대신 정적 팩터리 메서드를 고려하라

전통적인 인스턴스 얻는 방법은 public 생성자 입니다. 정적 팩터리 메서드(static factory method; 디자인 패턴의 팩터리 메서드와 다릅니다.)

```java

public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}
```

###정적 팩터리 메서드 장점

**이름을 가질 수 있습니다.**

**호출될 때마다 인스턴스를 새로 생성하지 않아도 됩니다.** 

불변 클래스(아이템 17)는 인스턴스를 미리 만들어 놓거나 캐싱하여 객체를 재활용할 수 있습니다. 반복되는 요청에 같은 객체를 반환하면 언제 어느 인스턴스를 살아 있게 할지를 통제할 수 있습니다. 인스턴스를 통제하면 클래스를 싱글턴(아이템 3)이나 인스턴스화 불가(아이템 4)로 만들 수 있습니다. 또한 불변 값 클래스(아이템 17)에서 동치인 인스턴스가 단 하나뿐임을 보장할 수 있습니다.(a == b일때만 a.equals(b)가 성립).

**반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있습니다.** 

반환할 객체의 클래스를 자유롭게 선택할 수 있게 하는 `엄청난 유연성`을 선물합니다. API를 만들 때 이 유연성을 응용하면 구현 클래스를 공개하지 않고도 객체를 반환할 수 있어 API를 작게 유지할 수 있습니다.

**입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있습니다.** 

반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없습니다. (Item01.EnumSet: 원소개수에 따라 다른 인스턴스 반환)

**정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 됩니다.** 

이런 유연함은 서비스 제공자 프레임을 만드는 근간이 됩니다.(대표적으로 JDBC)


**정적 팩터리 메서드 단점**

상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없습니다. 

정적 팩터리 메서드는 개발자가 찾기 어렵습니다. 생성자처럼 API설명에 드러나지 않으니 정적 팩터리 방식 클래스를 인스턴스화할 방법을 알아내야 합니다. 따라서 API 문서를 잘 써놓고 메서드 이름도 널리 알려진 규약에 따라 짓는 식으로 문제를 완화해줘야 합니다.

```java
//정적 팩터리 메서드에 흔히 사용하는 명명방식

from: 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 매서드
Date d = Date.from(instant);

of: 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING)

valueOf: = from과 of의 더 자세한 버전
BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);

instance 혹은 getInstance: (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지는 않습니다.
StackWalker luke = StackWalker.getInstance(options);

create 혹은 newInstance: instance 혹은 getInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장합니다.
Object newArray = Array.newInstance(classObject, arrayLen);

getType: getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 씁니다. "Type"은 팩터리 메서드가 반환할 객체의 타입입니다.
FileStore fs = Files.getFileStore(path);

newType: newInstance와 같으나, getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 씁니다. "Type"은 팩터리 메서드가 반환할 객체의 타입입니다.
BufferedReader br = Files.newBufferedReader(path);

type: getType과 newType의 간결한 버전
List<Complaint> litany = Collections.list(lagacyLitancy);



```