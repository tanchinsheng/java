/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q541.lambda;

import java.util.ArrayList;
import java.util.Iterator;
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
    public void filterData(List<Data> dataList, Predicate<Data> p) {
        Iterator<Data> i = dataList.iterator();
        while (i.hasNext()) {
            if (p.test(i.next())) {
                i.remove();
            }
        }
    }

    public static void main(String[] args) {

        List<Data> al = new ArrayList<>();
        Data d = new Data(1);
        al.add(d);
        d = new Data(2);
        al.add(d);
        d = new Data(3);
        al.add(d);

        //INSERT METHOD CALL HERE
        //new app().filterData(al, d -> d.value % 2 == 0);
        new app().filterData(al, (Data x) -> x.value % 2 == 0);
        System.out.println(al);
    }

}
