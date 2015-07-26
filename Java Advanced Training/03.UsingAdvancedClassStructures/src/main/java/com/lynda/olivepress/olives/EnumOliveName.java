/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lynda.olivepress.olives;

/**
 *
 * @author cstan
 */
public enum EnumOliveName {

    KALAMATA("Kalamata"), LIGURIO("Ligurio"), PICHOLINE("Picholine"), GOLDEN("Golden");

    private final String nameAsString;

    private EnumOliveName(String nameAsString) {
        this.nameAsString = nameAsString;
    }

    @Override
    public String toString() {
        return nameAsString;
    }

}
