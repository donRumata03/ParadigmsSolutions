package queue;


/**
 * Apart from contracts written in this file for all functions,
 * they correctly implement functions from queue interface
 * and inherit their contracts
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
     * @param queue is not null
     */
    public static void enqueue(ArrayQueueADT queue, final Object element) {
        ensureCapacity(queue, queue.size + 1);

        queue.elements[head(queue)] = element;
        queue.size++;
    }

    /**
     * @param queue is not null
     */
    public static Object dequeue(ArrayQueueADT queue) {
        var res = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.tail = nextCircularPosition(queue, queue.tail);
        queue.size--;

        return res;
    }


    /**
     * @param queue is not null
     */
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    /**
     * @param queue is not null
     */
    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }

    /**
     * @param queue is not null
     */
    public static void clear(ArrayQueueADT queue) {
        queue.elements = null;
        queue.size = 0;
        queue.tail = 0;
    }

    /**
     * @param queue is not null
     */
    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.tail];
    }

    /**
     * @param queue is not null
     */
    public static void push(ArrayQueueADT queue, final Object element) {
        ensureCapacity(queue, queue.size + 1);

        queue.tail = previousCircularPosition(queue, queue.tail);
        queue.elements[queue.tail] = element;
        queue.size++;
    }

    /**
     * @param queue is not null
     */
    public static Object peek(ArrayQueueADT queue) {
        return queue.elements[previousCircularPosition(queue, head(queue))];
    }

    /**
     * @param queue is not null
     */
    public static Object remove(ArrayQueueADT queue) {
        int removedPosition = previousCircularPosition(queue, head(queue));

        var res = queue.elements[removedPosition];
        queue.elements[removedPosition] = null;
        queue.size--;
        // queue.tail stays at the same position

        return res;
    }

    /**
     * @param queue is not null
     */
    public static int indexOf(ArrayQueueADT queue, Object element) {
        for (
            int indexFromTheirHead = 0, arrayPosition = queue.tail;
            indexFromTheirHead < queue.size;
            indexFromTheirHead++, arrayPosition = nextCircularPosition(queue, arrayPosition)
        ) {
            if (queue.elements[arrayPosition].equals(element)) {
                return indexFromTheirHead;  // Their head is still weird…
            }
        }

        return -1;
    }

    /**
     * @param queue is not null
     */
    public static int lastIndexOf(ArrayQueueADT queue, Object element) {
        for (
            int indexFromTheirHead = queue.size - 1, arrayPosition = previousCircularPosition(queue, head(queue));
            indexFromTheirHead >= 0;
            indexFromTheirHead--, arrayPosition = previousCircularPosition(queue, arrayPosition)
        ) {
            if (queue.elements[arrayPosition].equals(element)) {
                return indexFromTheirHead; // Their head is weird…
            }
        }

        return -1;
    }
}
