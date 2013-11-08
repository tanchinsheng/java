package part13complexdatasstructures;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * You can build up arbitrarily complicated data structures in Java using the
 * Collections framework classes.
 */
public class App {

    private static String[] vehicles = {"ambulance", "helicopter", "lifeboat"};
    private static String[][] drivers = {
        {"Fred", "Sue", "Pete"},
        {"Sue", "Richard", "Bob", "Fred"},
        {"Pete", "Mary", "Bob"}
    };
    
    public static void main(String[] args) {
        Map<String, Set<String>> personnel = new HashMap<>();
        for (int i = 0; i < vehicles.length; i++) {
            String vehicle = vehicles[i];
            String[] driverList = drivers[i];
            Set<String> driverSet = new LinkedHashSet<>();
            for (String driver: driverList) {
                driverSet.add(driver);
            }
            personnel.put(vehicle, driverSet);
        }
        
        { // Brackets just to scope driversList variable so can use again later
            // Example usage
            Set<String> driverList = personnel.get("helicopter");
            for (String driver : driverList) {
                System.out.println(driver);
            }
        }
        
        for (String vehicle : personnel.keySet()) {
            System.out.print(vehicle + ": ");
            Set<String> driverList = personnel.get(vehicle);
            
            for (String driver: driverList) {
                System.out.print(driver + " ");
            }
            System.out.println();
        }
        
    }   
    

}
