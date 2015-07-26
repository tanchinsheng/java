/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cstan
 */
public class Pair<T1, T2> {

    private final T1 firstItem;
    private final T2 secondItem;

    public Pair(T1 firstItem, T2 secondItem) {
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    public T1 getFirstItem() {
        return firstItem;
    }

    public T2 getSecondItem() {
        return secondItem;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
