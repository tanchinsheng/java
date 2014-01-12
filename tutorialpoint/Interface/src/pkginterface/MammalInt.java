/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkginterface;

/**
 *
 * @author cstan
 */
/* File name : MammalInt.java */
public class MammalInt implements Animal{

   public void eat(){
      System.out.println("Mammal eats");
   }

   public void travel(){
      System.out.println("Mammal travels");
   } 

   public int noOfLegs(){
      return 0;
   }


} 