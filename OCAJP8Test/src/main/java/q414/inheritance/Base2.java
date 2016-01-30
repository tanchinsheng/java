package q414.inheritance;

class Base {

    private float f = 1.0f;

    void setF(float f1) {
        this.f = f1;
    }
}

class Base2 extends Base {

    private float f = 2.0f;

    //1
    //protected void setF(float f1){ this.f = 2*f1; }
    public void setF(float f1) {
        this.f = 2 * f1;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
