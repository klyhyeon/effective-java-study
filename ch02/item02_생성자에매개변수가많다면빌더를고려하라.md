# 02. 생성자에 매개변수가 많다면 빌더를 고려하라

선택적 매개변수가 많을 때 생성자만으로는 적절히 대응하기가 어려울 수 있다. 예를 들어 식품의 영양정보 항목이 1회 내용량, 총 n회 제공량, 1회 제공량 칼로리, 총 칼로리, 총 지방, 트랜스 지방, 포화지방 등 여러 선택 항목으로 이루어질 수 있으며 개중 다수는 0인 값 혹은 필요없는 매개변수도 있을 것이다. 


- 점층적 생성자 패턴
- 자바빈즈 패턴
- 빌더 패턴 

## 02-1 점층적 생성자 패턴
원하지 않는 매개변수에도 값을 정해줘야한다.
매개변수가 많아지면 코드의 각 값이 무엇을 의미하는지 헷갈릴 수 있고, 갯수도 주의깊게 세어야 한다.

```java

public class NutritionFacts {
    private final int servingSize;  
    private final int servings;     
    private final int calories;     
    private final int fat;          
    private final int sodium;       
    private final int carbohydrate; 

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings,
                          int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings,
                          int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings,
                          int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }
    public NutritionFacts(int servingSize, int servings,
                          int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize  = servingSize;
        this.servings     = servings;
        this.calories     = calories;
        this.fat          = fat;

```

```java
NutritionFacts cocaCola = new NutritionFacts(240, 8. 100, 0, 35, 27);
```

## 02-2 자바 빈즈 패턴

객체 하나를 만들기 위해서는 메서드를 여러개 호출해야 한다.
객체가 완전히 생성되기 전까지는 일관성이 무너진 상태에 놓인다.

```java

public class NutritionFacts {
  
    private int servingSize  = -1; // 필수; 기본값 없음
    private int servings     = -1; // 필수; 기본값 없음
    private int calories     = 0;
    private int fat          = 0;
    private int sodium       = 0;
    private int carbohydrate = 0;

    public NutritionFacts() { }

    public void setServingSize(int val)  { servingSize = val; }
    public void setServings(int val)     { servings = val; }
    public void setCalories(int val)     { calories = val; }
    public void setFat(int val)          { fat = val; }
    public void setSodium(int val)       { sodium = val; }
    public void setCarbohydrate(int val) { carbohydrate = val; }

    public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts();
        cocaCola.setServingSize(240);
        cocaCola.setServings(8);
        cocaCola.setCalories(100);
        cocaCola.setSodium(35);
        cocaCola.setCarbohydrate(27);
    }
}

```
## 02-3 빌드 패턴 

NutritionFacts는 불변클래스가 될 수 있다.
빌더의 세터 메서드들은 빌더 자신을 반환하기 때문에 연쇄 호출이 가능해진다. (메서드를 따로 여러번 호출해주지 않아도 된다.)
계층적으로 설계된 클래스와 함께 쓰기에 좋다. **(02-5)**

```java

public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // 필수 매개변수
        private final int servingSize;
        private final int servings;

        // 선택 매개변수 - 기본값으로 초기화한다.
        private int calories      = 0;
        private int fat           = 0;
        private int sodium        = 0;
        private int carbohydrate  = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }

        public Builder calories(int val)
        { calories = val;      return this; }
        public Builder fat(int val)
        { fat = val;           return this; }
        public Builder sodium(int val)
        { sodium = val;        return this; }
        public Builder carbohydrate(int val)
        { carbohydrate = val;  return this; }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize  = builder.servingSize;
        servings     = builder.servings;
        calories     = builder.calories;
        fat          = builder.fat;
        sodium       = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
                .calories(100).sodium(35).carbohydrate(27).build();
    }
}


```



## 02-4 인스턴스 생성 비교 


```java

NutritionFacts cocaCola = new NutritionFacts(240, 8. 100, 0, 35, 27);

public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts();
        cocaCola.setServingSize(240);
        cocaCola.setServings(8);
        cocaCola.setCalories(100);
        cocaCola.setSodium(35);
        cocaCola.setCarbohydrate(27);
}
    
public  static  void  main(String[] args)  { 
	NutritionFacts cocaCola =  new NutritionFacts.Builder(240,  8).calories(100).sodium(35).carbohydrate(27).build();
}

```

## 02-5 계층적 클래스 패턴에 적용

피자 (Pizza.java) [상위] 
- 뉴욕 피자  (Nypizza.java) [하위]
- 칼조네 피자 (Calzone.java) [하위]

```java
package dami.ch01;

import java.util.EnumSet;
import java.util.Set;
import java.util.Objects;

public abstract class Pizza {

    public enum Topping {
        HAM, MUSHROOM, ONION, PEEPER, SAUSAGE
    }

    final Set<Topping> toppings;
							// 01 - 연쇄 메서드 지원: 재귀적 제너릭 이용 
    abstract static class Builder<T extends Builder<T>>{
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        
        public T addTopping(Topping topping){
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }
        
        abstract Pizza build();
		
		//02 - 연쇄 메서드 지원: 하위 클래스에서 self() 오버라이딩 하여 this 반환하도록 설정 해줘야 한다.
        protected abstract T self();

    }

    Pizza(Builder<?> builder){
        toppings = builder.toppings.clone();
    }

    
}

```

```java
package dami.ch01;
import java.util.Objects;

public class NyPizza extends Pizza{

    public enum Size{
        SMALL, MEDIUM, LARGE
    }
    
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder>{

        private final Size size;
        
		//크기를 필수 파라미터로 받음
        public Builder(Size size){
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build(){
            return new NyPizza(this);
        }

        @Override
        protected Builder self(){
            return this;
        }
    }

    private NyPizza(Builder builder){
        super(builder);
        size = builder.size;
    }  
}

```

```java

package dami.ch01;

public class Calzone extends Pizza{

    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder>{
        private boolean sauceInside=false;

		//소스(불린)을 필수 매개변수로 받음
        public Builder sauceInside(){
            sauceInside = true;
            return this;
        }

        @Override
        public Calzone build(){
            return new Calzone(this);
        }
        
        @Override
        protected Builder self(){
            return this;
        }
    }
    
    private Calzone(Builder builder){
        super(builder);
        sauceInside = builder.sauceInside;

    }
    
}

```

```java

//선언부 
NyPizza nyPizza01 = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
Calzone calZone = new Calzone.Builder().sauceInside().addTopping(HAM).build();

```