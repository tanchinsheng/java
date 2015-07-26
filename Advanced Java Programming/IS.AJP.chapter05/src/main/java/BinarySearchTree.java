/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cstan
 */
public class BinarySearchTree {

    public Node root;

    public BinarySearchTree() {
        root = null;
    }

    public void insert(int data) {
        Node newNode = new Node();
        newNode.data = data;
        if (root == null) {
            root = newNode;
        } else {
            Node current = root;
            Node parent;
            while (true) {
                parent = current;
                if (data < current.data) {
                    current = current.left;
                    if (current == null) {
                        parent.left = newNode;
                        break;
                    }
                } else {
                    current = current.right;
                    if (current == null) {
                        parent.right = newNode;
                        break;
                    }
                }
            }
        }
    }

    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(Node n) {
        if (n != null) {
            inOrder(n.left);
            System.out.println(n.getData());
            inOrder(n.right);
        }
    }

    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node n) {
        if (n != null) {
            System.out.println(n.getData());
            preOrder(n.left);
            preOrder(n.right);
        }
    }

    public int min() {
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.getData();
    }

    public int max() {
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.getData();
    }
}
