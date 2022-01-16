# Item 39. 명명 패턴보다 애너테이션을 사용하라.

- 테스트 프레임워크인 JUnit은 버전 3까지 테스트 메서드 이름을 test로 시작하게끔 명명 패턴을 적용했었다. 이는 효과적인 방법이지만 단점이 크다.
    - 오타가 나면 안 된다. ex) tset-
    - 올바른 프로그램 요소에서만 사용되리라 보증할 방법이 없다. JUnit은 테스트 메소드 이름에만 적용된 명명 패턴에만 관심을 가지는데, 개발자가 클래스 이름에 test를 추가한 경우, 개발자가 의도한 테스트는 전혀 수행되지 않는다.
    - 프로그램 요소를 매개변수로 전달할 마땅한 방법이 없다. 예를 들어 특정 예외를 던져야만 성공하는 테스트가 있다고 해보자. 기대하는 예외 타입을 테스트에 매개변수로 전달해야 하는 상황에서, 예외의 이름을 테스트 메서드 이름에 덧붙이는 방법도 있지만, 보기도 나쁘고 깨지기도 쉽다.
- 애너테이션은 이 모든 문제를 해결해주는 멋진 개념으로, JUnit도 버전 4부터 전면 도입하였다.

```java
/**
 * 테스트 메서드임을 선언하는 애너테이션이다.
 * 매개변수 없는 정적 메서드 전용이다.
 */
 
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
```

- meta-annotation : 애너테이션 선언에 다는 애너테이션
- **@Retention(RetentionPolicy.RUNTIME) : @Test가 런타임에도 유지되어야 한다는 표시**
- **@Target(ElementType.METHOD) : @Test 가 반드시 메서드 선언에서만 사용돼야 한다고 알려준다. 따라서 클래스 선언, 필드 선언 등 다른 프로그램 요소에는 달 수 없다.**
- 마커 애너테이션 / @Test 애너테이션이 클래스의 의미에 직접적인 영향을 주지는 않는다. 그저 이 애너테이션에 관심 있는 프로그램에게 추가 정보를 제공할 뿐이다. 더 넓게 이야기하면, 대상 코드의 의미는 그대로 둔 채 그 애너테이션에 관심 있는 도구에서 특별한 처리를 할 기회를 준다.

```java
import java.lang.reflect.*;

public class RunTests {
   public static void main(String[] args) throws Exception {
      int tests = 0;
      int passed = 0;
      Class<?> testClass = Class.forName(args[0]);
      for (Method m : testClass.getDeclaredMethods()) {
         if ( m.isAnnotationPresent(Test.class)) {
            tests++;
            try {
               m.invoke(null);
               passed++;
            } catch (InvocationTargetException wrappedExc) {
               Throwable exc = wrappedExc.getCause();
               System.out.println(m + " 실패: " + exc);
            } catch (Exception exc) {
               System.out.println("잘못 사용한 @Test: " + m);
            }
         }
      }
      System.out.printf("성공: %d, 실패\: %d%n",
               passed, tests - passed);
   }
}
```

**명시한 예외를 던져야만 성공하는 테스트 메서드용 애너테이션**

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
   Class<? extends Throwable> value();
}
```

- 이 애너테이션의 매개변수 타입은 Class<? extends Throwable> 이다. 여기서의 와일드카드 타입은 많은 의미를 담고 있다. "Throwable을 확장한 클래스의 Class 객체"라는 뜻이며, 따라서 모든 예외(와 오류) 타입을 다 수용한다.

```java
if (m.isAnnotationPresent(ExceptionTest.class)) {
   tests++;
   try {
      m.invoke(null);
      System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
   } catch (InvocationTargetException wrappedEx) {
      
			Throwable exc = wrappedEx.getCause();
      Class<? extends Throwable> excType =
         m.getAnnotation(ExceptionTest.class).value();
      
			if (excType.isInstance(exc)) {
         passed++;
      } else {
         System.out.printf(
               "테스트 %s 실패: 기대한 예외 %s, 발생한 예외 %s%n",
                  m, excType.getName(), exc);
      }
   } catch (Exception exc) {
      System.out.println("잘못 사용한 @ExceptionTest: " + m);
   }
}
```

- 원소가 여럿인 배열을 지정할 때는 다음과 같이 원소들을 중괄호로 감싸고 쉼표로 구분해주기만 하면 된다.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
   Class<? extends Throwable>[] value();
}
```

```java
@ExceptionTest({ IndexOutOfBoundsException.class, NullPointerException.class })
```

**여러 개의 값을 받는 애너테이션**

- 배열 매개변수를 사용하는 대신 애너테이션에 @Repeatable 메타애너테이션을 다는 방식
- @Repeatable을 단 애너테이션은 하나의 프로그램 요소에 여러 번 달 수 있다.
- 단, 주의할 점이 있다.
    1. 첫 번째, @Repeatable을 단 애너테이션을 반환하는 '컨테이너 애너테이션'을 하나 더 정의하고, @Repeatable에 이 컨테이너 애너테이션의 class 객체를 매개변수로 전달해야 한다.
    2. 두 번째, 컨테이너 애너테이션은 내부 애너테이션 타입의 배열을 반환하는 value 메서드를 정의해야 한다.
    3. 마지막으로 컨테이너 애너테이션 타입에는 적절한 보존 정책 @Retention과, 적용 대상 @Target을 명시해야 한다. 그렇지 않으면 컴파일 되지 않을 것이다.

```java
// 반복 가능한 애너테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)
public @interface ExceptionTest {
   Class<? extends Throwable> value();
}

// 컨테이너 애너테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTestContainer {
   ExceptionTest[] value();
}

@ExceptionTest(IndexOutOfBoundsException.class)
@ExceptionTest(NullPointerException.class)
public static void doublyBad() { ... }
```

```java
if (m.isAnnotationPresent(ExceptionTest.class) || 
      m.isAnnotationPresent(ExceptionTestContainer.class)) {
         tests++;
      // ...
 }
```

아주 간단하지만 애너테이션이 명명 패턴보다 낫다는 점은 확실히 보여준다. 애너테이션으로 할 수 있는 일을 명명 패턴으로 처리할 이유는 없다.