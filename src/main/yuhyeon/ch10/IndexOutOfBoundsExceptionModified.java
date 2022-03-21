package yuhyeon.ch10;

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
