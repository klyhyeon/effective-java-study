package yuhyeon.ch10;

public class ExceptionStackTrace {

    public static void main(String[] args) {
        stackTraceExample(5);
    }

    private static void stackTraceExample(int i) {
        try {
            int[] arr = new int[1];
            arr[i] = 1;
        } catch (IndexOutOfBoundsException e) {
//            throw e;
            IndexOutOfBoundsExceptionModified ioobem = new IndexOutOfBoundsExceptionModified(0, 1, i);
            System.out.println("lowerBound::: " + ioobem.getLowerBound());
            System.out.println("upperBound::: " + ioobem.getUpperBound());
            System.out.println("index::: " +  ioobem.getIndex());
            throw ioobem;
        }
    }
}
