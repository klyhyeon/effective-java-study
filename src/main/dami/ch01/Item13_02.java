package dami.ch01;

import java.lang.reflect.Array;
import java.util.Arrays;

class Item13_02 implements Cloneable{
    int i;
    int[] arr;

    Item13_02(int i, int[] arr){
        this.i = i;
        this.arr = arr;
    }

    public Item13_02 clone(){
        try{
            return (Item13_02) super.clone();
        }catch(CloneNotSupportedException e){
            throw new AssertionError();
        }
    }

    public Item13_02 copy(){
        try{
            Item13_02 result = (Item13_02) super.clone();
            result.arr = arr.clone();
            return result;
        }catch (CloneNotSupportedException e){
            throw new AssertionError();
        }
    }
    
    public static void main(String args[]){

        Item13_02 test01 = new Item13_02(1, new int[]{1,2,3});
        Item13_02 test02 = test01.clone();

        System.out.println(Arrays.toString(test02.arr));  // [1,2,3]
        
        test02.arr[0] = 100;
        System.out.println(Arrays.toString(test01.arr));  //[100, 2, 3] test01이 참조하는 배열이 바뀌어버림
        System.out.println(Arrays.toString(test02.arr));

        Item13_02 test03 = new Item13_02(2, new int[]{1,2,3});
        Item13_02 test04 = test03.copy();
        test04.arr[0] = 100;
        System.out.println(Arrays.toString(test03.arr));  
        System.out.println(Arrays.toString(test04.arr));

    }
}
