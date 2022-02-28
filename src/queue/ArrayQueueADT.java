package queue;


/**
 * Model: infinite sequence a[0..+inf], integer L, integer R
 * Invariant: for i \in [L, R): a[i] != null
 *
 * Let immutable(l, r): for i \in [l, r): a'[i] == a[i]
 * Let `cL`: L' == L
 * Let `cR`: R' == R
 */
public class ArrayQueueADT {
    private Object[] elements = null; // If not null, has capacity > 0
    private int tail = 0;
    private int size = 0;

    private static int head(ArrayQueueADT queue) {
        return (queue.tail + queue.size) % queue.elements.length;
    }

    // implied order from model is: [queue.tail, head) in circular space of `queue.elements`
    // queue is produced from head to «right» and consumed from queue.tail to «right»
    // Imagine a dinosaur is eating its queue.tail…

    private static int capacity(ArrayQueueADT queue) {
        return queue.elements == null ? 0 : queue.elements.length;
    }

    private static int nextCircularPosition(ArrayQueueADT queue, int pos) {
        return (pos + 1) % queue.elements.length;
    }

    private static int previousCircularPosition(ArrayQueueADT queue, int pos) {
        return pos == 0 ? queue.elements.length - 1 : pos - 1;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int requiredCapacity) {
        if (capacity(queue) < requiredCapacity) {
            int newCapacity = (capacity(queue) + 1) * 2;
            Object[] newArray = new Object[newCapacity];

            for (int oldPtr = queue.tail, newPtr = 0;
                newPtr < size(queue);
                oldPtr = nextCircularPosition(queue, oldPtr), newPtr++
            ) {
                newArray[newPtr] = queue.elements[oldPtr];
            }

            queue.elements = newArray;
            queue.tail = 0;
            // queue.size remains unchanged
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
    public static void enqueue(ArrayQueueADT queue, final Object element) {
        ensureCapacity(queue, queue.size + 1);

        queue.elements[head(queue)] = element;
        queue.size++;
    }

    /**
     * Pred: nE
     * Post:
     *  — immutable(L + 1,  R)
     *  — L' = L + 1
     *  — cR
     *  — Ret = a[L]
     */
    public static Object dequeue(ArrayQueueADT queue) {
        var res = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.tail = nextCircularPosition(queue, queue.tail);
        queue.size--;

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
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    /**
     * Pred: true
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = (R - L == 0)
     */
    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }

    /**
     * Pred: true
     * Post:
     *  — 0 <= L = R
     */
    public static void clear(ArrayQueueADT queue) {
        queue.elements = null;
        queue.size = 0;
        queue.tail = 0;
    }

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = a[L]
     */
    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.tail];
    }

    /**
     * Pred: element != null
     * Post:
     *  — immutable(L,  R)
     *  — cR
     *  — L' = L - 1
     *  — a[L'] = element
     */
    public static void push(ArrayQueueADT queue, final Object element) {
        ensureCapacity(queue, queue.elements.length + 1);

        queue.tail = previousCircularPosition(queue, queue.tail);
        queue.elements[queue.tail] = element;
        queue.size++;
    }

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = a[R - 1]
     */
    public static Object peek(ArrayQueueADT queue) {
        return queue.elements[previousCircularPosition(queue, head(queue))];
    }

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R - 1)
     *  — cL
     *  — R' = R - 1
     *  — Ret = a[R - 1]
     */
    public static Object remove(ArrayQueueADT queue) {
        int removedPosition = previousCircularPosition(queue, head(queue));

        var res = queue.elements[removedPosition];
        queue.elements[removedPosition] = null;
        queue.size--;
        // queue.tail stays at the same position

        return res;
    }

    public static int lastIndexOf(ArrayQueueADT queue, Object element) {
        for (
            int indexFromHead = 0, arrayPosition = previousCircularPosition(queue, head(queue));
            indexFromHead < queue.size;
            indexFromHead++, arrayPosition = previousCircularPosition(queue, arrayPosition)
        ) {
            if (queue.elements[arrayPosition].equals(element)) {
                return queue.size - 1 - indexFromHead; // Their head is weird…
            }
        }

        return -1;
    }


    public static int indexOf(ArrayQueueADT queue, Object element) {
        for (
            int indexFromHead = queue.size - 1, arrayPosition = queue.tail;
            indexFromHead >= 0;
            indexFromHead--, arrayPosition = nextCircularPosition(queue, arrayPosition)
        ) {
            if (queue.elements[arrayPosition].equals(element)) {
                return queue.size - 1 - indexFromHead;  // Their head is still weird…
            }
        }

        return -1;
    }
}
