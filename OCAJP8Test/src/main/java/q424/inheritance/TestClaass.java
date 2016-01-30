package q424.inheritance;

interface Flyer {

    String getName();
}

class Bird implements Flyer {

    public String name;

    public Bird(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Eagle extends Bird {

    public Eagle(String name) {
        super(name);
    }
}

public class TestClaass {

    public static void main(String[] args) throws Exception {

        Flyer f = new Eagle("American Bald Eagle");//PRINT NAME HERE
        System.out.println(f.getName());
        System.out.println(((Bird) f).name);
        System.out.println(((Bird) f).getName());
        System.out.println(f.name);
        System.out.println(Eagle.name);//name is not a static field in class Eagle.
        System.out.println(Eagle.getName(f));//This option doesn't make any sense.
    }

}
