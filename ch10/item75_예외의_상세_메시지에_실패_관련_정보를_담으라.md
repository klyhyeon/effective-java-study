## 예외의 상세 메시지에 실패 관련 정보를 담으라

예외 스택 추적은(Stack trace) 예외 객체의 toString 메서드를 호출해서 얻는 문자열입니다. 자세한 정보를 얻는 게 디버깅에 도움이 되기 때문에 발생한 예외에 관여한 모든 매개변수와 필드의 값을 실패 메시지에 담아야 합니다.

```java
public class ExceptionStackTrace {

    public static void main(String[] args) {
        stackTraceExample(5);
    }

    private static void stackTraceExample(int i) {
        try {
            int[] arr = new int[1];
            arr[i] = 1;
        } catch (IndexOutOfBoundsException e) {
            IndexOutOfBoundsExceptionModified ioobem = new IndexOutOfBoundsExceptionModified(0, 1, i);
            System.out.println("lowerBound::: " + ioobem.getLowerBound());
            System.out.println("upperBound::: " + ioobem.getUpperBound());
            System.out.println("index::: " +  ioobem.getIndex());
            throw ioobem;
        }
    }
}
```

무엇을 고쳐야할 지 알려주는 로그가 좋은 로그입니다.

```java

public class IndexOutOfBoundsExceptionModified extends IndexOutOfBoundsException {

    private int lowerBound;
    private int upperBound;
    private int index;

    public IndexOutOfBoundsExceptionModified(int lowerBound, int upperBound, int index) {
        //실패를 포착하는 상세메시지 생성
        super(String.format(
                "최솟값: %d, 최댓값: %d, 인덱스: %d",
                lowerBound, upperBound, index
        ));

        //프로그램에서 이용할 수 있도록 실패 정보를 저장
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.index = index;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public int getIndex() {
        return index;
    }
}

```

최종 사용자에게 보여줄 에러 메시지와 상세 메시지를 혼동해선 안됩니다. 최종 사용자에겐 친절한 안내 메시지가 필요한 반면, 예외 메시지는 가독성보다는 담긴 내용이 훨씬 중요합니다.

