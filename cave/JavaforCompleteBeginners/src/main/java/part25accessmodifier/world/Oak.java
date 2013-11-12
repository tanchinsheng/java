/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part25accessmodifier.world;

public class Oak extends Plant {
     
    public Oak() {
         
        // Won't work -- type is private
        // type = "tree";
         
        // This works --- size is protected, Oak is a subclass of plant.
        this.size = "large";
         
        // No access specifier; works because Oak and Plant in same package
        this.height = 10;
    }
 
}