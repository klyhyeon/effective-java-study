package dami;

public class Item06 {

    //언박싱 발생 
    long sum(){
        Long sum = 0L;
        for (long i = 0 ; i<= Integer.MAX_VALUE; i++)
            sum+= i;
        return sum;
    }

    long sumPT(){
        long sum = 0L;
        for (long i = 0 ; i<= Integer.MAX_VALUE; i++)
            sum+= i;
        return sum;
    }


    public static void main(String[] args){

        //스트링 선언 1
        String a = "hello";
        String b = "hello";          //String Constant Pool & String literal
        System.out.println(a == b); //동일성 비교 (인스턴스 주소값 비교)
        System.out.println(a.equals(b));    //동질성 비교 (객체 내부 값 비교)
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());

        //스트링 선언 2: 새로운 인스턴스 생성, 메모리 할당 
        String newA = new String("hello");
        String newB = new String("hello");
        System.out.println(newA == newB);
        System.out.println(newA.equals(newB));
        System.out.println(newB.intern());
        
        // 언박싱 발생 
        Item06 prac = new dami.Item06();
        long beforetime = System.currentTimeMillis();
        prac.sum();
        long aftertime = System.currentTimeMillis();
        System.out.println((aftertime - beforetime)/100000.0);

        long beforetimePT = System.currentTimeMillis();
        prac.sumPT();
        long aftertimePT = System.currentTimeMillis();
        System.out.println((aftertimePT - beforetimePT)/100000.0);

        // Boolean practice
        boolean bl = new Boolean("true");
        boolean bl2 = Boolean.valueOf("true");
        boolean bl3 = Boolean.parseBoolean("true");

    }
}



