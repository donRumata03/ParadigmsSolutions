package queue;

/**
 * The only thing you need to know about this class is that it correctly implements Queue (checked by kgeorgiy)
 */
public class ArrayQueue implements Queue {
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

    private int previousCircularPosition(int pos) {
        return pos == 0 ? elements.length - 1 : pos - 1;
    }

    private void ensureCapacity(int requiredCapacity) {
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

    public void enqueue(final Object element) {
        ensureCapacity(size + 1);

        elements[head()] = element;
        size++;
    }

    public Object dequeue() {
        var res = elements[tail];
        elements[tail] = null;
        tail = nextCircularPosition(tail);
        size--;

        return res;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        elements = null;
        size = 0;
        tail = 0;
    }

    public Object element() {
        return elements[tail];
    }

    public void push(final Object element) {
        ensureCapacity(elements.length + 1);

        tail = previousCircularPosition(tail);
        elements[tail] = element;
        size++;
    }

    public Object peek() {
        return elements[previousCircularPosition(head())];
    }

    public Object remove() {
        int removedPosition = previousCircularPosition(head());

        var res = elements[removedPosition];
        elements[removedPosition] = null;
        size--;
        // tail stays at the same position

        return res;
    }

    public int indexOf(Object element) {
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

    public int lastIndexOf(Object element) {
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
}