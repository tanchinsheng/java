/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part27EncapsulationandtheAPIDocs;

/**
What is encapsulation and what’s the point of it? We’ll take a look at the meaning 
* and benefits of encapsulation in this tutorial. We’ll also see an example of 
* encapsulation in the real world by looking at the API document for the String class. 
* In this tutorial we’ll build on the previous tutorial on the public, private and 
* protected keywords to look at why and when you actually use these keywords for 
* best programming practice.
 */
class Plant {
     
    // Usually only static final members are public
    public static final int ID = 7;
     
    // Instance variables should be declared private, 
    // or at least protected.
    private String name;
     
    // Only methods intended for use outside the class
    // should be public. These methods should be documented
    // carefully if you distribute your code.
    public String getData() {
        String data = "some stuff" + calculateGrowthForecast();
         
        return data;
    }
     
    // Methods only used the the class itself should
    // be private or protected.
    private int calculateGrowthForecast() {
        return 9;
    }
     
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
     
     
}
 
 
public class App {
 
    public static void main(String[] args) {
         
    }
 
}