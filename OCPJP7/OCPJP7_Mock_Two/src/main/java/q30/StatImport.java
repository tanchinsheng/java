/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q30;

//import static q30.mock.PQR.*; // for foo();
//import static q30.mock.XYZ.*; // for pqr.foo();
//import static q30.mock.XYZ.pqr; // for pqr.foo();
public class StatImport {

    public static void main(String[] args) {
        //STMT
        foo();
        pqr.foo();
        PQR.foo();
        XYZ.pqr.foo();

    }

}
