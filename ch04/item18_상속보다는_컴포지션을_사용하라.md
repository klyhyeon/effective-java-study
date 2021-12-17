## item18_상속보다는_컴포지션을_사용하라

### 서론

코드를 재사용하는 방법들 중에 상속도 있지만 항상 최선은 아닙니다. 그 이유는, 잘못 사용하면 오류를 내기 쉽기 때문입니다. 단, 패키지 내에서의 상속이나, 1인 프로그래머가 관리하는 상속은 안전합니다. 하지만 이 책에서의 `상속`은 클래스가 다른 구현상속을 말합니다.

메서드 호출과 달리 상속은 `캡슐화`를 깨뜨립니다. 무슨 말이냐하면, 상위 클래스가 어떻게 구현됐느냐에 따라 하위 클래스가 오작동할 수 있습니다.

### 예제코드
(실제로 정상작동 하지 않습니다. 3이 아닌 6이 반환됩니다. 그 이유는?)
```java

/* 실행메서드 */
public static void main(String[] args) {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(List.of("틱", "탁탁", "펑"));
        System.out.println(s.getAddCount());
}

/* 구현클래스 */
public class InstrumentedHashSet<E> extends HashSet<E> {

    //추가된 원소의 수
    private int addCount = 0;

    public InstrumentedHashSet() {

    }

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}
```

HashSet의 super.addAll(c)는 add 메서드를 호출하는데, 이 때 InstrumentedHashSet의 add 함수가 호출된 것입니다. (yuhyeon.ch04.Executor 코드를 실행하고 yuhyeon.ch04.InstrumentedHashSet 코드에 디버깅을 걸면 볼 수 있습니다.)

### 해결

addAll 메서드를 다른 식으로 재정의해서 해결할 수 있습니다. 예를 들어 주어진 컬렉션을 순회하며 원소 하나당 add 메서드를 한 번만 호출하는 것입니다.

**_**하지만 여전히 문제가 생길 여지가 있습니다.**_** 그리고 상위 클래스의 메서드 동작을 다시 구현하는 방식은 어렵고, 시간되 더 들고, 오류를 내거나 성능을 떨어뜨릴 수도 있습니다. 또한 하위 클래스에서 접근할 수 없는 `private` 필드를 써야된다면 아예 불가능합니다.

### 컴포지션을 사용하자

상속이 가진 문제점(상위 클래스의 메서드가 추가되었을 때 같은 시그니처를 가지고 있을 시 충돌이 일어날 수 있고, 하위 클래스에서 상위 클래스 메서드를 정의하지 못할 수 있음)을 해결할 수 있는 해결책은 `새로운 클래스를 만들고 private 필드로 기존 클래스의 인스턴스를 참조: 컴포지션`하는 것입니다. 

기존 클래스의 대응하는 메서드를 호출해 그 결과를 반환하며, 새 클래스의 메서드들을 전달메서드(forwarding method)라고 부릅니다. 이런 방법은 기존 클래스의 내부 구현 방식의 영향에서 벗어나며, 심지어 기존 클래스에 새로운 메서드가 추가되더라도 전혀 영향받지 않습니다. 

### 컴포지션을 이용해 수정된 예제코드

하나는 래퍼 클래스, 다른 하나는 전달 메서드만으로 이뤄진 재사용 가능한 전달(구체) 클래스입니다.

(래퍼 클래스의 핵심은?)

```java

/* 실행메서드 */
/* 컴포지션 방식은 올바른 결과값 3이 출력됨 */

public static void main(String[] args) {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(List.of("틱", "탁탁", "펑"));
        System.out.println("unfixed::: " + s.getAddCount());

        /* 래퍼 클래스와 전달 메서드를 사용한 컴포지션 방식*/
        Set<String> set = new TreeSet<>();
        set.add("틱");
        set.add("탁탁");
        set.add("펑");
        FixedInstrumentedHashSet<String> fs = new FixedInstrumentedHashSet<>(set);
        fs.addAll(set);
        System.out.println("fixed::: " + fs.getAddCount());
}

/* 래퍼 클래스 */
public class FixedInstrumentedHashSet<E> extends ForwardingSet<E> {

    //추가된 원소의 수
    private int addCount = 0;

    public FixedInstrumentedHashSet(Set<E> s) {
        super(s);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}

/* 구체 클래스 */
public class ForwardingSet<E> implements Set<E> {

    private final Set<E> s;
    public ForwardingSet(Set<E> s) {
        this.s = s;
    }

    @Override
    public int size() {
        return s.size();
    }

    @Override
    public boolean isEmpty() {
        return s.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return s.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return s.iterator();
    }

    @Override
    public Object[] toArray() {
        return s.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return s.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return s.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return s.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return s.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return s.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return s.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return s.removeAll(c);
    }

    @Override
    public void clear() {
        s.clear();
    }
}

```

래퍼 클래스의 핵심은 임의의 Set에 `계측` 기능(구체 클래스)를 세워 새로운 Set으로 만들고 래퍼 클래스의 기본 생성자에 컴포지션으로 넣어 활용하는 것입니다.

다른 Set에 계측 기능을 덧씌운다는 뜻에서 `데코레이터 패턴`이라고 부릅니다. 

### 상속할 때 참고할 점

B가 A를 상속할 때, B가 A인가?를 생각해봐야 합니다. `is-a: B는 A이다`가 성립해야만 상속이 이뤄져야 합니다. 이런 관계가 성립해도, 캡슐화를 보장히고 결함의 전파를 막기위해 컴포지션을 사용해야 합니다. 






