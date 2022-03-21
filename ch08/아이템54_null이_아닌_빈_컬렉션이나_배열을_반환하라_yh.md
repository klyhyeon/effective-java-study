## 아이템54 null이 아닌 빈 컬렉션이나 배열을 반환하라

### 왜 null 대신 빈 컬렉션이나 배열을 반환?

null에 대한 에러 처리가 힘들기 때문입니다.

### 사용 방법(코드)

매번 새로운 배열이나 컬렉션을 할당한다면 heap에 저장되기 때문에 성능이 다소 저하될 수 있습니다. 따라서 `Collectins.EmptyList()`와 같은 불변 컬렉션을 넘기도록 합니다. 

배열의 경우 길이가 0인 배열을 만들어 반환해줍니다. 길이가 0인 배열은 모두 불변이기 때문입니다.

```java
    /* emptyList Java docs 구현코드 - EMPTY_LIST를 반환 */
    public static final <T> List<T> emptyList() {
        return (List<T>) EMPTY_LIST;
    }

    @SuppressWarnings("rawtypes")
    public static final List EMPTY_LIST = new EmptyList<>();

    /* emptyList - 예시코드 */
    @GetMapping
    public List<String> getNameList() {
        //이름의 리스트가 비었다.
        return Collections.emptyList();
    }
```

항상 리스트나 배열의 결과값이 없는 것이 아닌, 비었다고 생각하여 처리해주면 null 대신 빈 배열/리스트를 반환하는 습관을 들일 수 있습니다.

매번 새로 할당해주는 것보다 미리 선언해둔 길이 0인 불변 배열을 선언해주는 게 성능에 유리합니다.

```java
    
    private static final Name[] EMPTY_NAME_ARRAY = new Name[0];
    
    @GetMapping
    public List<String> getNameList() {
            //이름의 리스트가 비었다.
            return EMPTY_NAME_ARRAY;
    }
```