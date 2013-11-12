/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import part25accessmodifier.world.Plant;
 
 
public class Grass extends Plant {
    public Grass() {
         
        // Won't work --- Grass not in same package as plant, even though it's a subclass
        // System.out.println(this.height);
    }
}