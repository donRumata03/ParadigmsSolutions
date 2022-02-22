package queue;

import java.util.Arrays;

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
    private int tail = 0;
    private int size = 0;

    private int head() {
        return (tail + size) % elements.length;
    }

    // implied order from model is: [tail, head) in circular space of `elements`
    // queue is produced from head to «right» and consumed from tail to «right»
    // Imagine a dinosaur is eating its tail…

    private int capacity() {
        return elements == null ? 0 : elements.length;
    }

    private int nextCircularPosition(int pos) {
        return (pos + 1) % elements.length;
    }

    private void ensureCapacity(int requiredCapacity) {
        if (capacity() < requiredCapacity) {
            int newCapacity = (capacity() + 1) * 2;
            Object[] newArray = new Object[newCapacity];

            for (int oldPtr = tail, newPtr = 0; newPtr < size(); oldPtr = nextCircularPosition(oldPtr), newPtr++) {
                newArray[newPtr] = elements[oldPtr];
            }

            elements = newArray;
            tail = 0;
            // size remains unchanged
        }
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
        ensureCapacity(size + 1);

        elements[head()] = element;
        size++;
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
        var res = elements[tail];
        elements[tail] = null;
        tail = nextCircularPosition(tail);
        size--;

        return res;
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
        return size;
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
        elements = null;
        size = 0;
        tail = 0;
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
        return elements[tail];
    }
}
