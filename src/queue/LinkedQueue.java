package queue;


/**
 * The only thing you need to know about this class is that it correctly implements Queue (checked by kgeorgiy)
 */
public class LinkedQueue extends AbstractQueue {


    private static class Node {
        private Object value;
        private Node left;
        private Node right;

        private Node(Node left, Node right) {
            this.value = null;
            this.left = left;
            this.right = right;
        }

        public static Node emptyNode() {
            return new Node(null, null);
        }

        public Node(Object value, Node left, Node right) {
            assert value != null;

            this.value = value;
            this.left = left;
            this.right = right;
        }
    }


    private final Node leftPtr = Node.emptyNode();
    private final Node rightPtr = Node.emptyNode();

    public LinkedQueue() {
        super();
        leftPtr.right = rightPtr;
        rightPtr.left = leftPtr;
    }

    private void connect(Node left, Node right) {
        left.right = right;
        right.left = left;
    }

    private void makeNodeBefore(Node successor, Object element) {
        var newNode = new Node(element, successor.left, successor);

        connect(successor.left, newNode);
        connect(newNode, successor);
    }


    private void makeNodeAfter(Node predecessor, Object element) {
        makeNodeBefore(predecessor.right, element);
    }

    private void delinkNode(Node delinked) {
        var l = delinked.left;
        var r = delinked.right;

        l.right = r;
        r.left = l;
    }


    @Override
    protected void pushRightImpl(Object element) {
        makeNodeBefore(rightPtr, element);
    }

    @Override
    protected void pushLeftImpl(Object element) {
        makeNodeAfter(leftPtr, element);
    }

    @Override
    protected Object popLeftImpl() {
        var res = leftPtr.right.value;
        delinkNode(leftPtr.right);
        return res;
    }

    @Override
    protected Object popRightImpl() {
        var res = rightPtr.left.value;
        delinkNode(rightPtr.left);
        return res;
    }

    @Override
    protected Object viewLeftImpl() {
        return leftPtr.right.value;
    }

    @Override
    protected Object viewRightImpl() {
        return rightPtr.left.value;
    }

    @Override
    protected void clearImpl() {
        connect(leftPtr, rightPtr);
    }

    @Override
    public int indexOf(Object element) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object element) {
        return 0;
    }

}
