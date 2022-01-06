## item33_타입_안전_이종_컨테이너를_고려하라

**타입 이종 컨테이너란?**

`Set<E>, Map<K,V>, ThreadLocal<T>, AtomicReference<T>` 등은 제네릭이 단일원소 컨테이너로도 쓰이는 예입니다.

단점은 하나의 컨테이너에서 매개변수화할 수 있는 타입의 수가 제한된다는 것입니다. 만약 DB의 row는 임의 개수의 column을 가질 수 있을 때, 모든 열을 타입 안전하게 이용할 수 있다면 좋을 것입니다.

이와 같을 때 이종 컨테이너를 사용할 수 있습니다.

Favorites 클래스는 전역변수 Map이 있고 이종 타입으로 `Class<T>`를 key로 가집니다. 이때 쓰이는 Class 객체를 타입 토큰이라고 합니다.

이종 타입의 방식은 두 가지 제약을 가지는데 첫번째가 형변환을 어기는 때이며, (`HashSet<Integer>` 내에 String을 넣는다던지) 두번째는 실체화(item_28 참고; 배열은 실체화 돼 runtime 때 TypeException을 발견하고 리스트는 컴파일 때 발견못하면 runtime 때 exception 발견하지 못할 수 있음) 불가 타입에는 사용하지 못한다는 것입니다.

`ParameterizedTypeReference` (슈퍼 타입 토큰)

```java

public class Favorites {

    private Map<Class<?>, Object> favorites = new HashMap<>();

    public <T> void putFavorite(Class<T> type, T instance) {
        //첫번째 제약 해결. 동적 형변환(cast)를 사용해 타입이 일치함을 검증해줍니다.
        favorites.put(Objects.requireNonNull(type), type.cast(instance));
    }
    public <T> T getFavorite(Class<T> type) {
        //반환타입을 Object에서 T로 변환시켜 줍니다. 
        return type.cast(favorites.get(type));
    }

}
```

비한정적 제네릭을 사용했는데, 한정적 제네릭을 사용하고 싶다면 AnnotatedElement 인터페이스에 선언된 getAnnotation 메서드를 참고해보면 좋습니다.(item_39 어노테이션API)

getAnnotation() 메서드는 대상 타입의 인스턴스가 annotationClass 토큰 타입이 있다면 그 어노테이션을 반환하고 아니라면 null을 반환합니다. 

```java
public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
    Objects.requireNonNull(annotationClass);

    return (A) annotationData().annotations.get(annotationClass);
}
```

