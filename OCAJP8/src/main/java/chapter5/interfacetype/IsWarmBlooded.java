/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.interfacetype;

public interface IsWarmBlooded {

    boolean hasScales();

    public default double getTemperature() {
        return 10.0;
    }
}
