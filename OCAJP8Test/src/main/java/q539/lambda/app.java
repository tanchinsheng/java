/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q539.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public void printUsefulData(List<Data> dataList, Predicate<Data> p) {
        for (Data d : dataList) {
            if (p.test(d)) {
                System.out.println(d.value);
            }
        }
        dataList.stream().filter((d) -> (p.test(d))).forEach((d) -> {
            System.out.println(d.value);
        });
    }

    public static void main(String[] args) {

        List<Data> al = new ArrayList<>();
        al.add(new Data(1));
        al.add(new Data(2));
        al.add(new Data(3));

        //INSERT METHOD CALL HERE
//        new app().printUsefulData(al, (Data d) -> {
//            return d.value > 2;
//        });
        new app().printUsefulData(al, d -> d.value > 2);
    }

}
