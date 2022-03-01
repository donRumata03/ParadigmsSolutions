package queue;

/**
 * The only thing you need to know about this class is that it correctly implements Queue (checked by kgeorgiy)
 */
public class ArrayQueue extends AbstractQueue {
    private Object[] elements = null; // If not null, has capacity > 0
    private int tail = 0;

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

    @Override
    public void pushRightImpl(final Object element) {
        ensureCapacity(size + 1);

        elements[head()] = element;
    }

    @Override
    public void pushLeftImpl(final Object element) {
        ensureCapacity(elements.length + 1);

        tail = previousCircularPosition(tail);
        elements[tail] = element;
    }

    @Override
    public Object popLeftImpl() {
        var res = elements[tail];
        elements[tail] = null;
        tail = nextCircularPosition(tail);

        return res;
    }

    @Override
    public Object popRightImpl() {
        int removedPosition = previousCircularPosition(head());

        var res = elements[removedPosition];
        elements[removedPosition] = null;
        // tail stays at the same position

        return res;
    }

    @Override
    public Object viewLeftImpl() {
        return elements[tail];
    }

    @Override
    public Object viewRightImpl() {
        return elements[previousCircularPosition(head())];
    }

    @Override
    public void clearImpl() {
        elements = null;
        tail = 0;
    }

    @Override
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

    @Override
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
