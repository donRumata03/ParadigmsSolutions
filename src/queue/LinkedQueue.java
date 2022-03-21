package queue;


import java.util.function.Predicate;

/**
 * The only thing you need to know about this class is that it correctly implements Queue
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
    protected void clearImpl() {
        connect(leftPtr, rightPtr);
    }

    @Override
    Object rightmostIterator() {
        return rightPtr.left;
    }

    @Override
    Object leftmostIterator() {
        return leftPtr.right;
    }

    @Override
    Object stepLeft(Object iterator) {
        return ((Node)iterator).left;
    }

    @Override
    Object stepRight(Object iterator) {
        return ((Node)iterator).right;
    }

    @Override
    Object dereferenceIterator(Object iterator) {
        return ((Node)iterator).value;
    }

    @Override
    int filter(Predicate<Object> predicate) {
        Node node = leftPtr.right;
        int elementsLeft = 0;
        for (int i = 0; i < size; i++) {
            var nextNode = node.right;

            if (!predicate.test(node.value)) {
                connect(node.left, node.right);
            } else {
                elementsLeft++;
            }

            node = nextNode;
        }

        return elementsLeft;
    }
}
