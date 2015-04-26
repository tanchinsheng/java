/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question;

import java.util.concurrent.Callable;

/**
 *
 * @author cstan
 */
class CallableTaskD implements Callable<Integer> {

    @Override
    public void call() {
        System.out.println("In Callable.call");
    }
}
