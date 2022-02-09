## 아이템54 null이 아닌 빈 컬렉션이나 배열을 반환하라

에러 처리가 힘들다.

빈 배열을 새로 할당하는 게 낭비라면,Collections.EmptyList() 불변 객체를 사용할 수 있음

리스트가 없는 것이 아닌, 리스트가 비었다고 생각
```java

@GetMapping
public List<String> getNameList() {
    //이름의 리스트가 비었다!
        Collections.emptyList();
}
```