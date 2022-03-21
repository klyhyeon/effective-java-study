# [item81]. wait와 notify보다는 동시성 유틸리티를 애용하라.

## 프로세스와 쓰레드

프로세스: 프로그램이 메모리에 올라가있는 상태 <br />
쓰레드: 어떠한 프로그램 내에서, 특히 프로세스 내에서 실행되는 흐름의 단위를 말한다. 일반적으로 한 프로그램은 하나의 스레드를 가지고 있지만, 프로그램 환경에 따라 둘 이상의 스레드를 동시에 실행할 수 있다.

## 멀티태스킹과 멀티쓰레딩
멀티태스킹:  다수의 작업(혹은 프로세스, 이하 태스크)이 중앙 처리 장치(이하 CPU)와 같은 공용자원을 나누어 사용하는 것을 말한다. <br />
멀티쓰레딩: 하나의 프로세스 내에서 여러 쓰레드가 동시에 작업을 수행하는 것을 말한다.

## wait와 notify

자바 5에서 고수준의 동시성 유틸리티가 도입되면서 wait, notify를 쓸 이유가 거의 없다. <br />
java.util.concurrent의 동시성 유틸리티를 사용하자.

## java.util.concurrent

1. 실행자 프레임워크
2. 동시성 컬렉션
3. 동기화 장치

## 동시성 커렉션

동시성 컬렉션은 List, Queue, Map 같은 표준 컬렉션 인터페이스에 동시성을 가마해 구현한 고성능 컬렉션이다. <br />
**동시성 컬렉션에서 동시성을 무력화하는 건 불가능하며, 외부에서 락을 추가로 사용하면 오히려 속도가 느려진다.**

## 상태 의존적 메서드
동시성 컬렉션의 동시성을 무력화하지 못하기 때문에 여러 메서드를 원자적으로 묶어 호출하는 것도 못한다. 그래서 여러 동작을 하나의 원자적 동작으로 묶는 상태 의존적 메서드가 추가되었다. 몇몇 메서드는 매우 유용하여 일반 컬렉션 인터페이스의 디폴트 메서드 형태로 추가되었다.

예를 들면 putIfAbsent는 Map의 디폴트 메서드인데 인자로 넘겨진 key가 없을 때 value를 추가한다. 기존 값이 있으면 그 값을 반환하고 없는 경우에는 null을 반환한다. String의 intern 메서드를 아래와 같이 흉내를 낼 수 있다.

```java
private static final ConcurrentMap<String, String> map =
        new ConcurrentHashMap<>();

public static String intern(String s) {
    String result = map.get(s);
    if (result == null) {
        result = map.putIfAbsent(s, s);
        if (result == null) {
            result = s;
        }
    }
    return result;
}
```

동기화한 컬렉션보다 동시성 컬렉션을 사용해야 한다. 예를 들어 Collections의 synchronizedMap보다는 ConcurrentHashMap을 사용하는 것이 훨씬 좋다. 동기화된 맵을 동시성 맵으로 교체하는 것 하나만으로 성능이 개선될 수 있다.

## 동기화 장치
스레드가 다른 스레드를 기다릴 수 있게 하여 서로의 작업을 조율할 수 있도록 해준다. 대표적인 동기화 장치로는 CountDownLatch와 Semaphore가 있으며 CyclicBarrier와 Exchanger도 있다. 가장 강력한 동기화 장치로는 Phaser가 있다.

CountDownLatch는 하나 이상의 스레드가 또 다른 하나 이상의 스레드 작업이 끝날 때까지 기다린다. 생성자 인자로 받는 정수값은 래치의 countdown 메서드를 몇 번 호출해야 대기하고 있는 스레드들을 깨우는지 결정한다.

```java
> System.out.println("hello"));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    public static long time(Executor executor, int concurrency,
                            Runnable action) throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            executor.execute(() -> {
                // 타이머에게 준비가 됐음을 알린다.
                ready.countDown();
                try {
                    // 모든 작업자 스레드가 준비될 때까지 기다린다.
                    start.await();
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 타이머에게 작업을 마쳤음을 알린다.
                    done.countDown();
                }
            });
        }

        ready.await(); // 모든 작업자가 준비될 때까지 기다린다.
        long startNanos = System.nanoTime();
        start.countDown(); // 작업자들을 깨운다.
        done.await(); // 모든 작업자가 일을 끝마치기를 기다린다.
        return System.nanoTime() - startNanos;
    }
}
```
위 코드에서 executor는 concurrency 매개변수로 지정한 값만큼의 스레드를 생성할 수 있어야 한다. 그렇지 않으면 메서드 수행이 끝나지 않는데 이를 스레드 기아 교착 상태라고 한다. 또, 시간을 잴 때는 시스템 시간과 무관한 System.nanoTime을 사용하는 것이 더 정확하다.

## 핵심 정리

코드를 새로 작성한다면 wait와 notify를 쓸 이유가 거의 없다. 

