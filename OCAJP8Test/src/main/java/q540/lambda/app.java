/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q540.lambda;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public void processList(List<Data> dataList, Process p) {
        for (Data d : dataList) {
            p.process(d.value, d.value);
        }
    }

    public static void main(String[] args) {

        List<Data> al = new ArrayList<>();
        al.add(new Data(1));
        al.add(new Data(2));
        al.add(new Data(3));

        //INSERT METHOD CALL HERE
    }

}
