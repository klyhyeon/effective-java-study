package yuhyeon.ch01.item03;

public class ElvisSingleton {

    private ElvisSingleton() {}

    //멤버 필드로 인스턴스 제공
    public static final ElvisSingleton INSTANCE = new ElvisSingleton();
    //정적 메서드로 인스턴스 제공
    public static ElvisSingleton getInstance() {
        return INSTANCE;
    }
}
