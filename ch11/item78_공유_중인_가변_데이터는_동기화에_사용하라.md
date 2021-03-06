## 아이템 78 공유 중인 가변 데이터는 동기화에 사용하라

단일 스레드보다 동시성 프로그래밍은 어렵습니다. 하지만 멀티코어 프로세서의 힘을 제대로 활용하려면 반드시 내 것으로 만들어야하는 기술이기도 합니다.

### synchronized

해당 메서드는 해당 스레드나 블록을 한 스레드씩 수행하도록 보장합니다.

한 객체가 `일관된 상태`를 가지고 생성되고, 이 객체에 접근하는 메서드는 `그 객체에 락을 겁니다`. 락을 건 메서드는 객체의 상태를 확인하고 필요하면 수정합니다. 즉, 객체를 하나의 일관된 상태에서 다른 일관된 상태로 변화시킵니다. 동기화를 제대로 사용한다면 어떤 메서드도 이 객체의 상태가 `일관되지 않는 순간을 볼 수 없을 것`입니다.

또다른 중요한 기능은 동시성을 사용하지 않으면 한 스레드가 만든 변화를 다른 스레드에서 확인하지 못할수도 있다는 것입니다.

언어 명세상 `long`과 `double`을 제외한 변수를 읽고 쓰는 동작은 `원자적(atomic)` 입니다. 여러 스레드가 같은 변수를 동기화 없이 수정하는 중이라도, 항상 어떤 스레드가 정상적으로 저장한 값을 온전히 읽어옴을 보장한다는 것입니다.

그럼에도 동기화가 필요한 이유는 다른 스레드가 수정한 값이 나에게 `보이는가`를 보장하진 않기 때문입니다. 동기화는 배타적 실행뿐 아니라 스레드 사이의 안정적인 통신에 꼭 필요합니다.

### volatile

동기화하는 비용을 줄이고 성능을 늘이려면 `volatile`을 사용해도 됩니다. 락을 걸지 않기 때문에 배타적 수행은 일어나지 않고, 가장 최근에 기록된 변수를 읽도록 보장합니다. 하지만 연산자를 읽고 쓰는 중에 다른 스레드가 들어와 읽는다면 변경된 값을 돌려받지 못하는 `안전 실패(safety failure)`가 발생할 수 있습니다.

이 땐 메서드에 `synchronized`를 붙여줘야하며 변수의 `volatile`은 삭제해줘야 합니다.

### AtomicLong

atomic 라이브러리의 AtomicLong은 volatile의 최신화 읽기뿐 아니라 배타적 실행까지 지원합니다. 또한 성능도 동기화 버전보다 우수합니다.

### 마무리

앞서 언급된 멀티스레드의 비동기 문제들을 해결할 최선의 방법은 멀티스레드 환경에선 가변 데이터를 공유하지 않는 것입니다. `가변 데이터는 단일 스레드에서만 쓰도록 합니다.`

또한 프레임워크나 라이브러리를 깊이 이해하는 것도 중요합니다. 