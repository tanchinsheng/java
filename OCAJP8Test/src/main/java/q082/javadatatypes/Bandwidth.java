package q082.javadatatypes;

public class Bandwidth {

    public int available = 0;

    public int getAvailable() {
        return available;
    }

    public Bandwidth(int quota) {
        this.available = quota;
    }

    public void addMore(int more) {
        available += more;
    }

    public static void main(String[] args) {
        Bandwidth bw = new Bandwidth(100);
        //INSERT CODE HERE
        // bw.available = 0; // print 0
        bw.addMore(-bw.getAvailable()); // print 0
        System.out.println(bw.getAvailable());
    }

}
