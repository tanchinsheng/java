/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.lcom4;

/**
 *
 * @author cstan
 */


abstract class MyBean1 implements MyInterface1 {
   private String name;
   @Override
   public String getName() {
      return this.name;
   }
   @Override
   public void setName(String theName) {
      this.name = theName;
   }
}

abstract class MyBean2 extends MyBean1 implements MyInterface2 {
   private String phone;
   @Override
   public String getPhone() {
      return this.phone;
   }
   @Override
   public void setPhone(String thePhone) {
      this.phone= thePhone;
   }
}

public class MyPublishedBean extends MyBean2 implements MyPublishedInterface {
   //There is no any coding, it just a collected capabilities 
   //which will be published to other package. Some capabilities
   //may be hidden and use internally.
}
