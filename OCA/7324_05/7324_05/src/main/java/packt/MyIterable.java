package packt;

import java.util.Iterator;

public class MyIterable implements Iterable<Integer> {
    private MyIterator iterator;

    // Constructor
    public MyIterable() {
        iterator = new MyIterator();
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return iterator;
    }
    
}
