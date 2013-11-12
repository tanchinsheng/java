/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part25accessmodifier.world;

public class Field {

    private Plant plant = new Plant();

    public Field() {

        // size is protected; Field is in the same package as Plant.
        System.out.println(plant.size);
    }
}
