package yuhyeon.ch11;

import java.util.concurrent.TimeUnit;

public class StopThread {

    //가변적 데이터
    private static boolean stopRequested;

    private static synchronized void requestStop() {
        stopRequested = true;
    }

    private static synchronized boolean isStopRequested() {
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        //synchornized 하지 않으면 StopThread를 실행한 스레드와 backgroundThread가 일치하지 않기 때문에 stopRequested이 변경돼도 변수를 읽지 못하게 됩니다.
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            //이 부분은 OpenJDK vm hoisting(최적화) 기능 때문에 컴파일 시 변환실행 될수도 있습니다.
            //https://stackoverflow.com/questions/9338180/why-hotspot-will-optimize-the-following-using-hoisting
            /*
            The author assumes there that the variable done is a local variable,
            which does not have any requirements in the Java Memory Model to expose its value to other threads
            without synchronization primitives.

            Or said another way: the value of done won't be changed or viewed by any code other than what's shown here.

            In that case, since the loop doesn't change the value of done, its value can be effectively ignored,
            and the compiler can hoist the evaluation of that variable outside the loop, preventing it from
            being evaluated in the "hot" part of the loop.
            This makes the loop run faster because it has to do less work.
             */
            /* explicit 동시성이 없다면, hoisting은 loop 밖에서 실행돼도 되는 코드는 빼버림 */
//            while (!stopRequested) {
            while (isStopRequested()) {
//                System.out.println(i++);
                i++;
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(10);
        stopRequested = true;
//        requestStop();
    }
}
