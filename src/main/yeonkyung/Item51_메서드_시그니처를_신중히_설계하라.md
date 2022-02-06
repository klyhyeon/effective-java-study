# Item 51. 메서드 시그니처를 신중히 설계하라

### 메서드 시그니처

- 메서드의 이름과 매개변수의 순서, 타입, 개수를 의미 (오버로딩)
- 메서드의 리턴 타입과 예외 처리 부분은 메서드 시그니처가 아니다.

### **1. 메서드 이름을 신중히 짓자**

- 항상 **표준 명명 규칙**을 따라야 한다.
- 같은 패키지에 속한 다른 이름들과 일관되게 짓는다.
- 개발자 커뮤니티에서 널리 받아들여지는 이름을 사용한다.

### **2. 편의 메서드를 너무 많이 만들지 말자**

- 편의 메서드
    - ex) Collections의 swap, min, max 등
    - 메서드에 이름을 주어 좀 더 명확하게 무슨 일을 하는 지 말해줄 수 있으며, 기능 단위로 묶을 수 있고 재사용할 수 있다.
- 메서드가 너무 많은 클래스는 익히고, 사용하고, 문서화하고, 테스트하고 유지 보수하기 어렵다. 이를 구현하는 사람과 사용하는 사람 모두를 고통스럽게 한다.
- 클래스나 인터페이스는 자신의 각 기능을 완벽히 수행하는 메서드로 제공해야 한다. 아주 자주 쓰일 경우에만 별도의 약칭 메서드를 두도록 한다.

### **3. 매개 변수 목록은 짧게 유지하자**

- 4개 이하가 좋다.
- 특히 같은 타입의 매개변수가 연달아 여러 개 나오는 경우가 특히 해롭다. 순서를 바꿔 입력해도 그대로 컴파일되고 실행된다. 실수할 가능성이 높아진다.
- **매개변수를 줄여주는 방법 3가지**
    1. **여러 메서드로 쪼개서 매개변수를 줄이는 방법**
        
        ex) 리스트에서 지정된 범위의 부분 리스트에서 특정 요소를 찾는 경우
        
        ```java
        List<String> list = Lists.of("a", "b", "c", "d");
        
        // List<E> subList(int fromIndex, int toIndex);
        // int indexOf(Object o);
        List<String> newList = list.subList(1, 3);
        int index = newList.indexOf("b"); // 0
        ```
        
        - 기능을 두 가지로 분리 - 지정된 범위의 부분 리스트를 구하는 기능, 주어진 원소를 찾는 기능 (두 기능은 공통점이 없다 → 분리)
        - 두 기능을 분리해서 만든다면 다른 곳에서도 쉽게 조합해서 사용할 수 있다. → 공통점이 없도록, 직교성이 높게
        - 기능을 쪼개면 자연스럽게 중복이 줄고 결합성이 낮아지며, 코드를 수정하고 테스트하기 쉬워진다.
    
    1. **매개변수 여러 개를 묶어주는 도우미 클래스 생성**
        - 매개변수가 많다면 매개변수를 묶어줄 수 있는 클래스를 만들어서 하나의 객체로 전달한다. 특히 매개변수 몇 개를 하나의 독립된 개념으로 볼 수  있을 때 추천하는 방법이다.
        - 일반적으로 이런 도우미 클래스는 정적 멤버 클래스로 둔다.
        
        ```java
        // method(String gamerName, String rank, String suit)
        method(String gamerName, Card card)
        
        class Blackjack {
            // 도우미 클래스 (정적 멤버 클래스)
            static class Card {
                private String rank;
                private String suit;
            }
        
            public void method(gamerName, card);
        }
        ```
        

1. **빌더 패턴을 적용한 객체를 메서드 호출에 응용하기**
    - 도우미 클래스 + 빌더 패턴
    - 모든 매개변수를 하나로 추상화한 객체를 정의하고, 그 객체에 빌더 패턴을 적용
    - 클라이언트에서 setter로 값 설정, execute()로 매개변수 유효성 체크
    - 매개변수가 많으면서 그 중 일부는 생략해도 좋을 때 좋다.

### **4. 매개변수 타입으로는 클래스보다 인터페이스가 낫다.**

- 클라이언트에 특정 구현체만 사용하게 제한하지 않도록
- ex) 메서드에 HashMap을 넘기지 말고  Map을 넘겨라. 그러면 HashMap 뿐만 아니라 다른 Map 구현체도 인수로 건넬 수 있다.

### **5. boolean보다는 원소 2개짜리 enum 이 낫다**

- 열거 타입을 사용하면 코드를 읽고 쓰기가 쉬워진다. 나중에 선택지를 추가하기도 쉽다.
    
    ```java
    public enum TemperatureScale { FAHRENHEIT, CELSIUS }
    ```
    
    - TemperatureScale(true) 보다 TemperatureScale.newInstance(TemperatureScale.CELSIUS)가 하는 일을 명확히 알려준다.

[참고]

[https://github.com/Meet-Coder-Study/book-effective-java/blob/main/8장/51_메서드_시그니처를_신중히_설계하라_이호빈.md](https://github.com/Meet-Coder-Study/book-effective-java/blob/main/8%EC%9E%A5/51_%EB%A9%94%EC%84%9C%EB%93%9C_%EC%8B%9C%EA%B7%B8%EB%8B%88%EC%B2%98%EB%A5%BC_%EC%8B%A0%EC%A4%91%ED%9E%88_%EC%84%A4%EA%B3%84%ED%95%98%EB%9D%BC_%EC%9D%B4%ED%98%B8%EB%B9%88.md)