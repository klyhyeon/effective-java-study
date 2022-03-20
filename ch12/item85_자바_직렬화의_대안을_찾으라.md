## item 85 자바 직렬화의 대안을 찾으라

Java에 처음으로 직렬화가 도입되었을 때 어렵지 않게 *분산객체를 만들 수 있다는 것은 매력적이었지만, 보이지 않는 생성자, API와 구현 사이의 모호해진 경계, 잠재적인 정확성 문제, 성능, 보안, 유지보수성 등 그 대가가 컸습니다. 

실제로 *2016년 샌프란시스코 시영 교통국이 랜섬웨어 공격을 받아 무인발급기가 이틀간 마비되어 무료로 메트로를 타는 사태를 겪기도 했습니다. 

ObjectInputStream의 readObject 메서드를 호출하면서 객체 그래프가 역직렬화되는데, readObject 메서드는 (Serializable) 인터페이스를 구현했다면, 클래스패스 안의 거의 모든 타입의 객체를 만들어낼 수 있는 마법 같은 생성자입니다. 따라서 클래스패스 안의 모든 코드들이 공격 범위에 들어간다는 뜻입니다.

Java의 표준 라이브러리나 Apache 같은 서드파티 라이브러리도 공격 범위에 포함됩니다.


### 예제코드 - 역직렬화 폭탄
```java

    static byte[] bomb() {
        Set<Object> root = new HashSet<>();
        Set<Object> s1 = root;
        Set<Object> s2 = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            Set<Object> t1 = new HashSet<>();
            Set<Object> t2 = new HashSet<>();
            t1.add("foo"); //t1를 t2와 다르게 만든다.
            s1.add(t1); s1.add(t2);
            s2.add(t1); s2.add(t2);
            s1 = t1;
            s2 = t2;
        }
        return serialize(root);
    }
```

<img width="677" alt="image" src="https://user-images.githubusercontent.com/61368705/159151847-43326cd9-9a02-48c8-99fe-25912663bbd7.png">

`deserialize(bomb())`가 실행될 때, HashSet을 역직렬화 하기위해 2^100의 hashCode() 메서드(폭탄)이 호출됩니다. 따라서 문제들을 대처하려면 애초에 `신뢰할 수 없는 바이트 스트림을 역직렬화`하는 일 자체를 없애야 합니다.

### 대안: 크로스-플랫폼 구조화된 데이터 표현(cross-platform structured-data representation)

**대표구조: JSON, Protocol Buffers(protobuf)**

- Java 직렬화보다 간단
- 임의 객체 그래프를 자동으로 직렬화/역직렬화하지 않음
- 속성-값 쌍의 집합으로 구성된 간단하고 구조화된 데이터 객체를 사용


| JSON           | protobuf  |
|:-------------:| :-----:|
| Javascript 진영 | C++ 진영 |
| Text 기반      |   Binary 기반(pbtx 지원) |
| 텍스트 기반     |    성능효율 높음 |

### 꼭 직렬화를 해야겠다면

객체 역직렬화 필터링 (java.io.ObjectInputFilter)를 사용합시다.(Java 9에 추가)

객체 역직렬화 필터링은 데이터 스트림이 역직렬화되기 전에 필터를 설치하는 기능입니다. 클래스 단위로, 특정 클래스를 받아들이거나 거부할 수 있습니다. 


---
참고자료
- [RMI and object serialization](https://www.infoworld.com/article/2077275/rmi-and-object-serialization.html)
- [2016 SFMTA muni ransomware attack](https://arstechnica.com/information-technology/2016/11/san-francisco-transit-ransomware-attacker-likely-used-year-old-java-exploit/)
