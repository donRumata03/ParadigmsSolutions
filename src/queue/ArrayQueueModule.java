package queue;


/**
 * The only thing you need to know about this class is that it correctly implements Queue (checked by kgeorgiy)
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

    public static void enqueue(final Object element) {
        ensureCapacity(size + 1);

        elements[head()] = element;
        size++;
    }

    public static Object dequeue() {
        var res = elements[tail];
        elements[tail] = null;
        tail = nextCircularPosition(tail);
        size--;

        return res;
    }

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return size() == 0;
    }

    public static void clear() {
        elements = null;
        size = 0;
        tail = 0;
    }

    public static Object element() {
        return elements[tail];
    }

    public static void push(final Object element) {
        ensureCapacity(size + 1);

        tail = previousCircularPosition(tail);
        elements[tail] = element;
        size++;
    }

    public static Object peek() {
        return elements[previousCircularPosition(head())];
    }

    public static Object remove() {
        int removedPosition = previousCircularPosition(head());

        var res = elements[removedPosition];
        elements[removedPosition] = null;
        size--;
        // tail stays at the same position

        return res;
    }

    public static int indexOf(Object element) {
        for (
            int indexFromTheirHead = 0, arrayPosition = tail;
            indexFromTheirHead < size;
            indexFromTheirHead++, arrayPosition = nextCircularPosition(arrayPosition)
        ) {
            if (elements[arrayPosition].equals(element)) {
                return indexFromTheirHead;  // Their head is still weird…
            }
        }

        return -1;
    }

    public static int lastIndexOf(Object element) {
        for (
            int indexFromTheirHead = size - 1, arrayPosition = previousCircularPosition(head());
            indexFromTheirHead >= 0;
            indexFromTheirHead--, arrayPosition = previousCircularPosition(arrayPosition)
        ) {
            if (elements[arrayPosition].equals(element)) {
                return indexFromTheirHead; // Their head is weird…
            }
        }

        return -1;
    }
}
