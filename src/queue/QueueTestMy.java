package queue;

public class QueueTestMy {

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();

//        queue.enqueue(["do"]);
        queue.enqueue(10);
        queue.enqueue("Hello");

        System.out.println(queue.indexOf(10) == 1);
        System.out.println(queue.indexOf("Hello") == 0);

        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }

        assert queue.isEmpty();
    }
}
