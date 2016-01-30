package q122.javadatatypes.gc;

public class TestClass {

    public Object getObject() {
        Object obj = new String("aaaaa");//1
        Object objArr[] = new Object[1]; //2      
        objArr[0] = obj; //3
        obj = null;//4  
        objArr[0] = null;//5
        return obj;//6 GC
    }

    public static void main(String[] args) {
        TestClass o = new TestClass();
        o.getObject();
    }

}
