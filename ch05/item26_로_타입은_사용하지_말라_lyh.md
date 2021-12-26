## item26_로_타입은_사용하지_말라_lyh

제네릭을 사용하는 가장 큰 목적성은 컴파일 시 오류를 발견하기 위함입니다. 오류는 가능한 한 발생 즉시 발견하는 것이 좋기 때문입니다. 제네릭 클래스 혹은 제네릭 인터페이스는 클래스와 인터페이스 선언에 타입
매개변수를 사용하는 것을 말합니다.

`로 타입: 제네릭 타입에서 타입 매개변수를 전혀 사용하지 않을 때(List<E>의 로 타입은 List)`

`대표적 예시로 List<E>, List 인터페이스는 원소의 타입을 나타내는 타입 매개변수 E를 받습니다.`

### 로 타입보단 비한정적 와일드카드 타입

`Set<?>`, `Set`의 차이는, 컴파일 때 실행되는 제네릭 체크입니다. (package yuhyeon.ch05.GenericStudy 참고)

### 예외적으로 로 타입을 사용해야할 경우

1. Class 리터럴을 사용할 경우

```java

private static void genericClassLiteral(){
    Class<String> classLiteral=String.class;
}
```

2. instanceof 연산자를 사용할 경우

```java

if(o instanceof Set){
    Set<?> s 
}
```

런타임 시 제네릭 타입 정보가 지워지므로 instanceof 연산자는 비한정적 와일드카드 타입 이외의 매개변수화 타입에는 적용할 수 없습니다.