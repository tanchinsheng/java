/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_4;

/**
 *
 * @author cstan
 */
public class StrLastError<S extends CharSequence> extends LastError<String> {

    public StrLastError(S s) {
    }

    void setError(S s) {
        System.out.println("StrLastError: setError");
    }
}
