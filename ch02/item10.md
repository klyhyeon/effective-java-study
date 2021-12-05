### Item 10. equals는 일반 규약을 지켜 재정의하라

- 객체(주로 값 클래스)의 논리적 동치성을 확인해야 하는데, 상위 클래스에 equals()가 논리적 동치성을 비교하도록 정의된 것이 아닌 경우
- Map, Set 등 Collection 객체들이 equals를 기반으로 값을 비교
- **재정의할 때 지켜야 할 일반 규약**
    - 반사성(reflexivity)
        - null이 아닌 모든 참조값 x에 대해 x.equals(x)는 true
        - 객체는 자기 자신과 같아야 한다.
    - 대칭성(symmetry)
        - null이 아닌 모든 참조값 x, y에 대해 x.equals(y)가 true이면 y.equals(x)도 true다.
    - 추이성(transitivity)
        - null이 아닌 모든 참조값 x, y, z에 대해 x.equals(y)가 true이고 y.equals(z)도 true이면 x.equals(z)는 true다.
    
    <aside>
    ❓ **구체 클래스를 확장해 새로운 값을 추가하면서 equals() 규약을 만족시킬 방법은 존재하지 않는다.** 하지만 상속 대신 컴포지션을 활용하면 우회하여 문제를 해결할 수 있다.
    
    </aside>
    
    - 일관성(consistency)
        - null이 아닌 모든 참조값 x, y에 대해 x.equals(y)를 반복해서 호출하면 항상 같은 값을 반환한다.
        - equals() 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안 된다. ex) URL
        - 항시 메모리에 존재하는 객체만을 사용한 결정적 계산만 수행해야 한다.
    - non-null
        - null이 아닌 모든 참조값 x, y에 대해 x.equals(null)은 false이다.
        - 동치성을 검사하기 위해서는 입력받은 객체를 적절한 타입으로 형변환을 해야하므로 먼저 instanceof 연산자로 올바른 타입인지 검사한다. 이 때 null인 경우 false를 반환하므로 null 검사를 따로 할 필요는 없다.
- **Equals() 구현방법**
    - == 연산자로 입력이 자기 자신의 참조인지 확인한다.
    - instanceof 연산자로 입력이 올바른 타입인지 확인한다.
    - 입력을 올바른 타입으로 형변환한다.
    - 입력 객체와 대응되는 '핵심' 필드들이 모두 일치하는지 하나씩 검사한다.
        - float, double을 제외한 기본 타입은 == 연산자로 비교
        - 참조 타입 필드는 equals()
        - float, double은 Float.compare(float, float), Double.compare(double, double)로 비교한다.