/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.lcom4;

/**
 *
 * @author cstan
 */
interface MyInterface1 {
   String getName();
   void setName(String name);
}

interface MyInterface2 extends MyInterface1 {
   String getPhone();
   void setPhone(String phone);
}

public interface MyPublishedInterface extends MyInterface1, MyInterface2 {
   //There is no any definition, it just a collected capabilities 
   //which will be published to other package. Some capabilities 
   //may be hidden and use internally.
}
