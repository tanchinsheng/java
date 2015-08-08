/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HashMap;

import java.util.HashMap;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("K1", "V1");
        hm.put("K2", "V2");
        hm.put("K3", "V3");
        System.out.println(hm);

        hm.put("K4", "V4");
        System.out.println(hm);

        String cap = hm.get("K2");
        System.out.println(cap);

        hm.remove("K3");
        System.out.println(hm);

    }

}
