## item29_이왕이면_제너릭_타입으로_만들라

제네릭 타입을 새로 만드는 일은 조금 더 어렵습니다. 

### 1단계: Object 기반 스택

```java

public class Stack {
    private Object[] elements...
    
    public Stack() {
        elements = new Object[DEFAULT_INITIA_CAPACITY];
    }
}
```



### 2단계: 제네릭 스택으로 가는 첫 단계(컴파일 되지 않음)
```java

public class Stack<E> {
    private E[] elements;
    
    public Stack() {
        elements = new E[DEFAULT_INITIA_CAPACITY];
    }
}
```

### 3단계; 비검사 형변환과 @SuppressWarning
```java

public class Stack<E> {
    private E[] elements;
    
    /* 1번 방식 */
    @SuppressWarnings("unchecked")
    public Stack() {
        elements = (E[]) new Object[DEFAULT_INITIA_CAPACITY];
    }
    
    /* 2번 방식 */
    @SuppressWarnings("unchecked") E result = (E) elements[--size];
}
```
어노테이션으로 경고를 숨깁니다. ClassCastException이 발생하지 않습니다.

### 제네릭 매개타입 제한되는 경우

primitive(기본) 타입의 경우
- int, double...

타입 매개변수에 제약을 두는 제네릭 타입도 있습니다.

### 타입 매개변수에 제약을 두는 경우(extends/implements)

```java

import java.util.concurrent.BlockingQueue;

class DelayQueue<E extends Delayed> implements BlockingQueue<E>
```

결론: 형 변환보다 제네릭을 사용해서 일일이 형변환 해주는 번거로움을 없앱시다. 실무에선 1번 방식을 더 선호합니다. 짧은 코드 = 가독성, 그리고 읽어들일때마다 원소를 체킹하는 2번과 달리 한번만 해주면 되는 편의성. 다만 힙오염(Object != E) 때문에 2번을 선호하는 사람들도 있습니다.


### 완성된? 코드
```java

public static void main(String[] args) {
    Stack<String> stack = new Stack<>();
    for (String arg: args) {
        stack.push(arg);    
    }
    whils (!stack.isEmpty()) {
        System.out.println(stack.pop.toUpperCase());
    }
}
```