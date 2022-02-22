package queue;

/**
 * Model: infinite sequence a[0..+inf], integer L, integer R
 * Invariant: for i \in [L, R): a[i] != null
 *
 * Let immutable(l, r): for i \in [l, r): a'[i] == a[i]
 * Let `cL`: L' == L
 * Let `cR`: R' == R
 */
public class ArrayQueue {
    private Object[] elements = null; // If not null, has capacity > 0
    private int head = 0;
    private int tail = 0;
    // implied order from model is: [tail, head) in circular space of `elements`
    // queue is produced from head to «right» and consumed from tail to «right»
    // Imagine a dinosaur is eating its tail…

    private void ensureCapacity(int requiredSize) {
        if (elements.length < requiredSize) {
            int newSize = (elements.length + 1) * 2;
            Object[] newArray = ;
        }
    }

    private int nextCircularPosition(int pos) {
        return (pos + 1) % elements.length;
    }

    /**
     * Pred: true
     * Post:
     *  — Constructs queue with 0 <= L = R
     */
    public ArrayQueue() {

    }

    /**
     * Pred: element != null
     * Post:
     *  — immutable(L,  R)
     *  — a[R] = element
     *  — R' = R + 1
     *  — cL
     */
    public void enqueue(final Object element) {
        model().addLast(element);
    }

    /**
     * Pred: true
     * Post:
     *  — immutable(L + 1,  R)
     *  — L' = L + 1
     *  — cR
     *  — Ret = a[L]
     */
    public Object dequeue() {
        return model().removeFirst();
    }

    /**
     * Pred: true
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = R - L
     */
    public int size() {
        return model().size();
    }

    /**
     * Pred: true
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = (R - L == 0)
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Pred: true
     * Post:
     *  — 0 <= L = R
     */
    public void clear() {
        model().clear();
    }

    /**
     * Pred: true
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = a[L]
     */
    public Object element() {
        return model().getFirst();
    }
}
