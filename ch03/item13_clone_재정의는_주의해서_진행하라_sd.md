# Item13. clone 재정의는 주의해서 진행하라 

## 1. cloneable 인터페이스와 Object 의 clone() 메소드
```java
package java.lang;

public interface Cloneable {
}
```

```java
protected native Object clone() throws CloneNotSupportedException;
```
- 메소드 하나 없는 Cloneable 인터페이스의 역할은 Object의 protected 메서드인 clone의 동작 방식을 결정한다.
- clone() 메소드를 호출하면 그 객체의 필드들을 하나하나 복사한 객체를 반환한다. 
- cloneable 을 구현하지 않은 클래스 인스터스에서 호출하면 CloneNotSupportedException을 던진다.

```java
class Item13 implements Cloneable {
    String name ;
    String Id;

    Item13(String name, String Id){
        this.name = name;
        this.Id = Id;
    }

    @Override
    public Item13 clone() throws CloneNotSupportedException{
        return (Item13) super.clone();
    }

    public static void main(String args[]){

        Item13 test01 = new Item13("Hello", "100");
        Item13 test02 = null;

        try{
            test02 = test01.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }

        System.out.println(test01.hashCode());  //1983025922
        System.out.println(test02.hashCode());  //1579526446
        System.out.println(test01 == test02);   //false
        System.out.println(test01.equals(test02));  //false

        test02.name = "Future";
        System.out.println(test01.name);  //Hello
        System.out.println(test02.name);  //Future

    }
    
}
```
  
## 2. 가변객체와 참조
- clone 메서드는 사실상 생성자와 같은 효과를 낸다. 원본 객체에 아무런 해를 끼치지 않는 동시에 복제된 객체의 불변식을 보장해야한다.
- 가변 객체를 참조하게 되는 경우, 원본 객체와 복제 객체가 같은 값을 참조하게 될 수 있다.

```java
class Item13_02 implements Cloneable{
    int i;
    int[] arr; // array 

    Item13_02(int i, int[] arr){
        this.i = i;
        this.arr = arr;
    }

    public Item13_02 clone(){
        try{
            return (Item13_02) super.clone();
        }catch(CloneNotSupportedException e){
            throw new AssertionError();
        }
    }
}
```

```java
    Item13_02 test01 = new Item13_02(1, new int[]{1,2,3});
    Item13_02 test02 = test01.clone();

    System.out.println(Arrays.toString(test02.arr));  // [1,2,3]
    
    test02.arr[0] = 100; //클론 객체 0번 값 변경 
    System.out.println(Arrays.toString(test01.arr));  //[100, 2, 3] test01, test02가 참조하는 값이 모두 바뀌어버림
    System.out.println(Arrays.toString(test02.arr));
```

- 배열의 clone메서드를 사용하여 내부정보를 복사한다.
  
```java

  public Item13_02 copy(){
      try{
          Item13_02 result = (Item13_02) super.clone();
          result.arr = arr.clone();  // 스택의 내부정보를 복사한다. 배열의 clone을 호출해준다.
          return result;
      }catch (CloneNotSupportedException e){
          throw new AssertionError();
      }
  }
    
```
```java
    Item13_02 test03 = new Item13_02(2, new int[]{1,2,3});
    Item13_02 test04 = test03.copy();
    test04.arr[0] = 100;
    System.out.println(Arrays.toString(test03.arr));  // [1, 2, 3]
    System.out.println(Arrays.toString(test04.arr));  // [100, 2, 3]
```