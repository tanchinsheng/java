/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q547.lambda;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author cstan
 */
class Data {

    int value;

    public Data(int x) {
        this.value = x;
    }

    public String toString() {
        return "" + value;
    }
}

class MyFilter {

    public boolean test(Data d) {
        return d.value == 0;
    }
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void filterData(ArrayList<Data> dataList, MyFilter f) {
        Iterator<Data> i = dataList.iterator();
        while (i.hasNext()) {
            if (f.test(i.next())) {
                i.remove();
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Data> al = new ArrayList<>();
        Data d = new Data(1);
        al.add(d);
        d = new Data(2);
        al.add(d);
        d = new Data(0);
        al.add(d);
        filterData(al, new MyFilter());//1     
        System.out.println(al);
    }

}
