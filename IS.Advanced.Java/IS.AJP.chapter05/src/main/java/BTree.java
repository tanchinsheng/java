/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cstan
 */
public class BTree {

    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        bst.insert(8);
        bst.insert(7);
        bst.insert(13);
        bst.insert(3);
        bst.insert(10);
        bst.insert(6);
        bst.insert(14);
        //bst.inOrder();
        bst.preOrder();

    }

}
