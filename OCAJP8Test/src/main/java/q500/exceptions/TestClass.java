/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q500.exceptions;

/**
 *
 * @author cstan
 */
void m1() throws Exception{
   try{
      // line1
   }
   catch (IOException e){
       throw new SQLException();
   }
   catch(SQLException e){
       throw new InstantiationException();
   }
   finally{
      throw new CloneNotSupportedException();   // this is not a RuntimeException.


}

}


public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
