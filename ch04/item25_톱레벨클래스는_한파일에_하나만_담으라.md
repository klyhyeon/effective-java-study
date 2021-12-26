## item25 톱레벨 클래스는 한 파일에 하나만 담으라
<br>
소스파일 하나에 톱레벨 클래스를 여러개 선언하더라도 자바 컴파일시 에러가 나지 않는다. <br>
한 클래스를 여러가지로 정의할 수 있지만, <br>
그 중 어느것을 사용할지는 어떤 소스를 먼저 컴파일 하느냐에 따라 달라진다. (해석: 같은 이름의 클래스가 여럿일 경우에는 컴파일 순서에 따라 값이 달라질 수 있다) <br>

<br>

### 배경

> 
> Main.java에 Utensil 클래스와 Dessert 클래스를 담고 있는데 
> 
> Utensil.java와 Dessert.java 모두 U,D 두 클래스 모두를 담고있다. 





```java
// Main.java
public class Main {

	public static void main(String[] args) {

		System.out.println(Utensil.Name + Dessert.Name);		
	}

}

```

```java
// Utensil.java
class Utensil {
	static final String Name = "pan";
}

class Dessert{
	static final String Name = "cake";
}
```

```java
// Dessert.java
class Utensil {
	static final String Name = "pot";
}

class Dessert{
	static final String Name = "pie";
}

```
### 문제상황


>컴파일 순서에 따라 출력값이 다르거나 "pancake" or "potpie"
>
>컴파일시 에러가 발생한다. 

```java
// 컴파일: javac Main.java Utensil.java
// 출력 pancake

// 컴파일: javac Dessert.java Main.java
// 출력 potpie

// 컴파일: javac Main.java Dessert.java
// error: compilation failed

```

### 해결책
톱레벨 클래스들(Utensil과 Dessert)를 서로 다른 소스 파일로 분리한다.<br>
다른 클래스에 딸린 부차적인 클래스라면(특정 클래스의 내부 클래스로 쓰이는 게 좋다면) 정적멤버 클래스로 만드는 것이 일반적으로 더 나을 것이다. (하기 예시)

```java
public class Test {
	
	public static void main(String[] args) {
		System.out.println(Utensil.Name+Dessert.Name);
	}
	
	private static class Utensil{
		static final String Name ="pan";
	}

	private static class Dessert{
		static final String Name = "cake";
	}
}
```
