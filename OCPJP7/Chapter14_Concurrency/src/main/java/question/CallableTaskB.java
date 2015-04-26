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
class CallableTaskB implements Callable {

    @Override
    public Integer call() {
        System.out.println("In Callable.call");
        return 0;
    }
}
