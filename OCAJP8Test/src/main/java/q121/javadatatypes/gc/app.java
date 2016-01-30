package q121.javadatatypes.gc;

public class app {

    public Object getObject(Object a) //0   
    {
        Object b = new Object(); //XXX
        Object c, d = new Object(); //1
        c = b; //2
        b = a = null; //3
        return c; //4 }
    }

    public static void main(String[] args) {

        app o = new app();
        o.getObject(new Object());

    }
}
