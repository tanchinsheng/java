/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semaphone;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cstan
 */
public class Connection {

    private static Connection instance = new Connection();

    private int connections = 0;

    private Connection() {
    }

    public static Connection getInstance() {
        return instance;
    }

    public void connect() {
        synchronized (this) {
            connections++;
            System.out.println("Current connections++: " + connections);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        synchronized (this) {
            connections--;
            System.out.println("Current connections--: " + connections);
        }

    }
}
