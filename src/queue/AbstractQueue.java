package queue;

import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {

    protected int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }


    @Override
    public void enqueue(Object element) {
        assert element != null;

        pushRightImpl(element);
        size++;
    }

    protected abstract void pushRightImpl(Object element);

    @Override
    public void push(Object element) {
        assert element != null;

        pushLeftImpl(element);
        size++;
    }

    protected abstract void pushLeftImpl(Object element);


    @Override
    public Object dequeue() {
        assert size != 0;

        Object res = popLeftImpl();
        size--;

        return res;
    }

    protected abstract Object popLeftImpl();

    @Override
    public Object remove() {
        assert size != 0;

        Object res = popRightImpl();
        size--;

        return res;
    }

    protected abstract Object popRightImpl();

    @Override
    public Object element() {
        return viewLeftImpl();
    }

    protected abstract Object viewLeftImpl();


    @Override
    public Object peek() {
        assert !isEmpty();

        return viewRightImpl();
    }

    protected abstract Object viewRightImpl();


    @Override
    public void clear() {
        clearImpl();
        size = 0;
    }

    protected abstract void clearImpl();


    // Should be done with a typesafe way via trait associated types…
    abstract Object rightmostIterator();

    abstract Object leftmostIterator();

    abstract Object stepLeft(Object iterator);

    abstract Object stepRight(Object iterator);

    abstract Object dereferenceIterator(Object iterator);

    abstract void filter(Predicate<Object> predicate);


    @Override
    public int indexOf(Object element) {
        Object iterator = leftmostIterator();

        for (
            int indexFromTheirHead = 0;
            indexFromTheirHead < size;
            indexFromTheirHead++, iterator = stepRight(iterator)
        ) {
            if (dereferenceIterator(iterator).equals(element)) {
                return indexFromTheirHead;  // Their head is still weird…
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object element) {
        Object iterator = rightmostIterator();

        for (
            int indexFromTheirHead = size - 1;
            indexFromTheirHead >= 0;
            indexFromTheirHead--, iterator = stepLeft(iterator)
        ) {
            if (dereferenceIterator(iterator).equals(element)) {
                return indexFromTheirHead;  // Their head is still weird…
            }
        }

        return -1;
    }

    @Override
    public void removeIf(Predicate<Object> predicate) {
        filter(Predicate.not(predicate));
    }

    @Override
    public void retainIf(Predicate<Object> predicate) {
        filter(predicate);
    }

    @Override
    public void takeWhile(Predicate<Object> predicate) {
        final Boolean[] allWereTrue = {true};
        filter(o -> allWereTrue[0] &= predicate.test(o));
    }

    @Override
    public void dropWhile(Predicate<Object> predicate) {
        final boolean[] hasMeetFalse = {false};
        filter(o -> hasMeetFalse[0] |= !predicate.test(o));
    }
}