/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lynda.javatraining.exceptions;

/**
 *
 * @author cstan
 */
public class WrongFileException extends Exception {

    private static final long serialVersionUID = 42L;

    @Override
    public String getMessage() {
        return "You chose the wrong file!";
    }

}
