## [item08] finalizer와 cleaner 사용을 피하라

### 가비지 컬렉터

아이템8을 공부하기 전에 자바 가비지 컬렉터(GC)에 대한 이해가 필요한것 같다.
(https://d2.naver.com/helloworld/1329)

### 자바의 소멸자(finalizer, cleaner)와 C++의 소멸자

자바는 두 가지 객체 소멸자를 제공한다.

1. finalizer(자바9 deprecated)
2. cleaner

finalizer는 **예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다**
cleaner는 **finalizer보다는 덜 위험하지만, 여전히 예측할 수 없고, 느리고 일반적으로 불필요하다.**

C++ 소멸자랑 자바의 소멸자는 다르다
다음은 Person 클래스를 C++, Java로 구현했다.

```c++
class Person {
    private:
        char * name;
        int age;

    public:
        Person(const char * name) {
            this.name = new char[strlnen(name) + 1];
            strcpy(this.name, name, strlen(name) + 1);
        }

        ~Person() {
            delete [] name;
        }
}
```

C++에서 소멸자는 특정 객체와 관련된 자원을 회수하는 보편적인 방법이다

```java
class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

자바에서는 접근할 수 없게 된 자원을 가비지 컬렉터가 담당한다. 프로그래머에게 아무런 작업도 요구하지 않는다.
C++에서 비메모리 자원을 회수할때는 소멸자를 사용하지만 자바는 try-with-resources, try-finally를 사용해 해결한다.
문제는 finalizer와 cleaner는 즉시 수행된다는 보장이 없다. 객체에 접근할 수 없게 된 후 finalizer나 cleaner가 실행되기까지 얼마나 걸릴지 알 수 없다. 즉, finalizer와 cleaner로는 제때 실행되어야 하는 작업은 절대 할 수 없다.
예: 파일 닫기를 finalizer, cleaner에게 맡기면 중대한 오류를 일으킬 수 있다.(시스템이 동시에 열 수 있는 파일 개수에 한계가 있기 때문)
**finalizer, cleaner를 얼마나 신속히 수행할지는 전적으로 가비지 컬렉터 알고리즘에 달렸다.**

### finalizer, cleaner 심각한 성능 문제도 동반한다

Autocloseable 객체를 생성하고 가비지 컬렉터가 수거하기 까지 12ns 걸린 반면, finalizer를 사용하면 550ns가
걸렸다. cleaner 클래스도 성능은 finalizer와 비슷하다. 하지만 안전망 형태로만 사용하면 훨씬 빨라진다.(66ns)

### finalizer를 사용한 클래스는 finalizer 공격에 노출되어 심각한 보안 문제를 일으킬 수 있다

```java
public class FinalizerAttack {
    static FinalizerAttack finalizerAttack;
    int value;

    public FinalizerAttack(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("마이너스 값");
        }
        this.value = value;
    }

    @Override
    protected void finalize() throws Throwable {
        finalizerAttack = this;
    }
}
```

**객체 생성을 막으려면 생성자에서 예외를 던지는 것만으로 충분하지만, finalizer가 있다면 그렇지도 않다.**
**final이 아닌 클래스를 finalizer 공격으로부터 방어하려면 아무 일도 하지 않는 finalize 메서드를 만들고 final로 선언하자.**

### AutoCloseable

그렇다면 도대체 어떻게 해야할까?? finalizer, cleaner 대신 AutoCloseable을 구현해주고, 클라이언트에서 인스턴스를 다 쓰고 나면 close 메서드를 호출하면 된다.(try-with-resources)
또한 close 메서드에서 이 객체는 더 이상 유효하지 않음을 필드에 기록하고 다른 메서드에서 이 필드를 검사해서 객체가 닫힌 후에 불렸다면 IllegalStateException을 던지는게 좋다.

```java
public class Person implements AutoCloseable {
    boolean isDead;

    public void eat() {
        if (isDead) {
            throw new IllegalStateException("소멸된 객체입니다.");
        }
        System.out.println("치킨 먹는중");
    }

    @Override
    public void close() throws Exception {
        System.out.println("사람이 죽었습니다.");
        isDead = true;
    }

    public static void main(String[] args) throws Exception {
        try (Person person = new Person()) {
            person.eat();
        }
    }
}
```

### 자바의 소멸자의 역할

그래도 finalizer, cleaner를 활용할 수 있는 곳은 없을까??

1. 자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망 역할(FileInputStream, FileOutputStream, ThreadPoolExecutor)
2. 네이티브 피어 객체의 자원을 회수할때

```java
public class Room implements AutoCloseable{

    private static final Cleaner cleaner = Cleaner.create();

    private static class State implements Runnable {
        int numJunkPiles;

        public State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        @Override
        public void run() {
            System.out.println("방 청소");
            numJunkPiles = 0;
        }
    }

    private final State state;

    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles) {
        state = new State(numJunkPiles);
        cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() throws Exception {
        cleanable.clean();
    }
}
```
