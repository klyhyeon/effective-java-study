package dami.ch01;

class Item13_01 implements Cloneable {
    String name ;
    String Id;

    Item13_01(String name, String Id){
        this.name = name;
        this.Id = Id;
    }

    @Override
    public Item13_01 clone() throws CloneNotSupportedException{
        return (Item13_01) super.clone();
    }

    public static void main(String args[]){

        Item13_01 test01 = new Item13_01("Hello", "100");
        Item13_01 test02 = null;

        try{
            test02 = test01.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }

        System.out.println(test01.hashCode());
        System.out.println(test02.hashCode());
        System.out.println(test01 == test02);
        System.out.println(test01.equals(test02));

        test02.name = "Future";
        System.out.println(test01.name);
        System.out.println(test02.name);

    }
    
}
