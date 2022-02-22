package queue;

public class QueueTestMy {

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();

        queue.enqueue(10);
        queue.enqueue("Hello");

        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }

        assert queue.isEmpty();
    }
}
