/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q414.inheritance;

/**
 *
 * Which of the following options is a valid example of overriding?
 */
class Base {

    private float f = 1.0f;

    void setF(float f1) {
        this.f = f1;
    }
}

class Base2 extends Base {

    /**
     * @param args the command line arguments
     */
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
