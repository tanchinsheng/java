/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caveofprogramming.designpatterns.demo1.view;

/**
 *
 * @author cstan
 */
public class MySingleton {

    private MySingleton() {
    }

    public static MySingleton getInstance() {
        return MySingletonHolder.INSTANCE;
    }
    
    public static class MySingletonHolder {
        private  static final MySingleton INSTANCE = new MySingleton();
    }

}
