## optional 반환은 신중히 하라

### optional을 잘못 사용하고 있는 대표적인 예시

```java

Optional<User> optionalUser = userRespository.findById(id);
User user1 = optionalUser.orElse(null);
```

API Note:
Optional is primarily intended for use as a method return type where there is a clear need to represent "no result," and where using null is likely to cause errors. A variable whose type is Optional should never itself be null; it should always point to an Optional instance.

 -> 요약하면 null 사용하지 않고 값을 반환해주기 위해서 Optional이 존재합니다.