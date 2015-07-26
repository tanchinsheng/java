/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lynda.javatraining.threads.sync;

/**
 *
 * @author cstan
 */
public class TargetClass {

    public void call(int threadId) {
        System.out.println("Calling thread from " + threadId);
    }
}
