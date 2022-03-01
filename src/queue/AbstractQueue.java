package queue;

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
}
