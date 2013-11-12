/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part25accessmodifier.world;
 
class Something {
     
}
 
public class Plant {
    // Bad practice
    public String name;
     
    // Accepatable practice --- it's final.
    public final static int ID = 8;
     
    private String type;
     
    protected String size;
     
    int height;
     
    public Plant() {
        this.name = "Freddy";
        this.type = "plant";
        this.size = "medium";
        this.height = 8;
    }
}