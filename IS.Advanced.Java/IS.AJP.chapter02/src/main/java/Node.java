
public class Node<T> {

    private final T data;
    public Node next;

    public Node(T data) {
        this.data = data;
        next = null;
    }

    public T getData() {
        return data;
    }

}
