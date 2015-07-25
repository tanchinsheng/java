
public class LinkedList {

    public static void main(String[] args) {
        Node<String> node1 = new Node("David");
        Node<String> node2 = new Node("Raymond");
        Node<String> node3 = new Node("Cynthia");
        node1.next = node2;
        node2.next = node3;
        Node<String> footer = new Node("Footer");
        footer.next = null;
        node3.next = footer;
        Node<String> dummy;
        dummy = node1;
        while (dummy.next != null) {
            System.out.println(dummy.getData());
            dummy = dummy.next;
        }
    }

}
