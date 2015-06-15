/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assessment;

import java.util.List;

class Chicken {
}

interface HenHouse {

    public List<Chicken> getChickens();
}

public class Q14_ChickenSong {

    public static void main(String[] args) {

        HenHouse house = ;
        Chicken chicken = house.getChickens().get(0);
        for (int i = 0; i < house.getChickens().size(); chicken = house.getChickens().get(i++)) {
            System.out.println("Cluck");
        }

    }

}
