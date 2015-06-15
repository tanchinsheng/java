/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4;

import java.util.ArrayList;
import java.util.List;

public class Q28 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.removeIf(s -> s.isEmpty());
        list.removeIf(s -> {
            s.isEmpty()
        });
        list.removeIf(s -> {
            s.isEmpty();
        });
        list.removeIf(s -> {
            return s.isEmpty();
        });
        list.removeIf(String s -> s.isEmpty()
        );
        list.removeIf((String s) -> s.isEmpty());

    }

}
