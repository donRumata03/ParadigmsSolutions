package queue;

/**
 * Model: infinite sequence a[0..+inf], integer L, integer R
 * Invariant: for i \in [L, R): a[i] != null
 *
 * Let immutable(l, r): for i \in [l, r): a'[i] == a[i]
 * Let `cL`: L' == L
 * Let `cR`: R' == R
 * Let `nE`: R - L > 0
 */
public class ArrayQueueModule {
    private static Object[] elements = null; // If not null, has capacity > 0
    private static int tail = 0;
    private static int size = 0;

    private static int head() {
        return (tail + size) % elements.length;
    }

    // implied order from model is: [tail, head) in circular space of `elements`
    // queue is produced from head to «right» and consumed from tail to «right»
    // Imagine a dinosaur is eating its tail…

    private static int capacity() {
        return elements == null ? 0 : elements.length;
    }

    private static int nextCircularPosition(int pos) {
        return (pos + 1) % elements.length;
    }

    private static int previousCircularPosition(int pos) {
        return pos == 0 ? elements.length - 1 : pos - 1;
    }

    private static void ensureCapacity(int requiredCapacity) {
        if (capacity() < requiredCapacity) {
            int newCapacity = (capacity() + 1) * 2;
            Object[] newArray = new Object[newCapacity];

            for (int oldPtr = tail, newPtr = 0;
                newPtr < size();
                oldPtr = nextCircularPosition(oldPtr), newPtr++
            ) {
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
    // Implicit Default Constructor

    /**
     * Pred: element != null
     * Post:
     *  — immutable(L,  R)
     *  — a[R] = element
     *  — R' = R + 1
     *  — cL
     */
    public static void enqueue(final Object element) {
        ensureCapacity(size + 1);

        elements[head()] = element;
        size++;
    }

    /**
     * Pred: nE
     * Post:
     *  — immutable(L + 1,  R)
     *  — L' = L + 1
     *  — cR
     *  — Ret = a[L]
     */
    public static Object dequeue() {
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
    public static int size() {
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
    public static boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Pred: true
     * Post:
     *  — 0 <= L = R
     */
    public static void clear() {
        elements = null;
        size = 0;
        tail = 0;
    }

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = a[L]
     */
    public static Object element() {
        return elements[tail];
    }

    /**
     * Pred: element != null
     * Post:
     *  — immutable(L,  R)
     *  — cR
     *  — L' = L - 1
     *  — a[L'] = element
     */
    public static void push(final Object element) {
        ensureCapacity(elements.length + 1);

        tail = previousCircularPosition(tail);
        elements[tail] = element;
        size++;
    }

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = a[R - 1]
     */
    public static Object peek() {
        return elements[previousCircularPosition(head())];
    }

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R - 1)
     *  — cL
     *  — R' = R - 1
     *  — Ret = a[R - 1]
     */
    public static Object remove() {
        int removedPosition = previousCircularPosition(head());

        var res = elements[removedPosition];
        elements[removedPosition] = null;
        size--;
        // tail stays at the same position

        return res;
    }

    public static int lastIndexOf(Object element) {
        for (
            int indexFromHead = 0, arrayPosition = previousCircularPosition(head());
            indexFromHead < size;
            indexFromHead++, arrayPosition = previousCircularPosition(arrayPosition)
        ) {
            if (elements[arrayPosition].equals(element)) {
                return size - 1 - indexFromHead; // Their head is weird…
            }
        }

        return -1;
    }


    public static int indexOf(Object element) {
        for (
            int indexFromHead = size - 1, arrayPosition = tail;
            indexFromHead >= 0;
            indexFromHead--, arrayPosition = nextCircularPosition(arrayPosition)
        ) {
            if (elements[arrayPosition].equals(element)) {
                return size - 1 - indexFromHead;  // Their head is still weird…
            }
        }

        return -1;
    }
}
