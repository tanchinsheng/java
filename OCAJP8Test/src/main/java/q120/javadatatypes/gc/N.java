package q120.javadatatypes.gc;

public class N {

    private M m = new M();

    public void makeItNull(M pM) {
        pM = null;
    }

    public void makeThisNull() {
        makeItNull(m);
    }

    public static void main(String[] args) {
        N n = new N();
        n.makeThisNull();
    }

}
