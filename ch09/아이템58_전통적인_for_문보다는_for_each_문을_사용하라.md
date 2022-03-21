# [item58]. 전통적인 for 문보다는 for-each 문을 사용하라.

## 전통적인 for문

### 컬렉션 순회하기 - 더 나은 방법이 있다.
```java
for (Iterator<Element> i = c.iterator(); i.hasNext(); ) {
    Element e = i.next();
}
```

### 배열 순회하기 - 더 나은 방법이 있다.
```java
for (int i = 0; i < a.length; i++) {
    // a[i]로 무언가를 한다.
}
```

**반복자와 인덱스 변수는 모두 코드를 지저분하게 할 뿐 우리에게 필요한 건 원소들뿐이다.** 더군다나 오류가 생길 가능성도 높아진다.

### 컬렉션과 배열을 순회하는 올바른 관용구
```java
for (Element e : elements) {
}
```

## 전통적인 for문의 단점
```java
public class Card {
    private final Suit suit;
    private final Rank rank;

    enum Suit { CLUB, DIAMOND, HEART, SPADE }
    enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        NINE, TEN, JACK, QUEEN, KING }

    static Collection<Suit> suits = Arrays.asList(Suit.values());
    static Collection<Rank> ranks = Arrays.asList(Rank.values());

    Card(Suit suit, Rank rank ) {
        this.suit = suit;
        this.rank = rank;
    }

    public static void main(String[] args) {
        List<Card> deck = new ArrayList<>();
        
        for (Iterator<Suit> i = suits.iterator(); i.hasNext(); )
            for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
                // NoSuchElementException 발생
                deck.add(new Card(i.next(), j.next())); 
    }
}
```

### 더 심각한 상황
```java
public class DiceRolls {
    enum Face { ONE, TWO, THREE, FOUR, FIVE, SIX }

    public static void main(String[] args) {
        Collection<Face> faces = EnumSet.allOf(Face.class);

        for (Iterator<Face> i = faces.iterator(); i.hasNext(); )
            for (Iterator<Face> j = faces.iterator(); j.hasNext(); )
                System.out.println(i.next() + " " + j.next());
    }
}
```

### 문제는 고쳤지만 보기 좋지 않다. 더 나은 방법이 있다~
```java
for (Iterator<Suit> i = suits.iterator(); i.hasNext(); )
    Suit suit = i.next();
    for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
        deck.add(new Card(suit, j.next())); 

```

### 컬렉션이나 배열의 중첩 반복을 위한 권장 관용구 
```java
for (Suit suit : suits)
    for (Rank rank : ranks)
        deck.add(new Card(suit, rank));
```

## for-each문을 사용할 수 없는 상황
- 파괴적인 필터링
- 변형
- 병렬 반복