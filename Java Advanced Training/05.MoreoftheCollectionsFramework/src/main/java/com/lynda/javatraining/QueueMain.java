package com.lynda.javatraining;

import com.lynda.javatraining.olives.Golden;
import com.lynda.javatraining.olives.Kalamata;
import com.lynda.javatraining.olives.Ligurio;
import com.lynda.javatraining.olives.Olive;
import com.lynda.javatraining.olives.Picholine;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class QueueMain {

    public static void main(String[] args) {
        LinkedList<Olive> list = new LinkedList<>();

        list.add(new Picholine());
        list.add(new Kalamata());
        list.add(1, new Golden());
        list.addFirst(new Ligurio());

        display(list);
        Olive o1 = list.peek();
        System.out.println(o1.getOliveName().toString());
        display(list);
        Olive o2 = list.poll();
        System.out.println(o2.getOliveName().toString());
        display(list);
    }

    static private void display(Collection<Olive> col) {
        System.out.println("List order: ");
        Iterator<Olive> iterator = col.iterator();
        while (iterator.hasNext()) {
            Olive olive = (Olive) iterator.next();
            System.out.println(olive.getOliveName().toString());
        }
    }

}
