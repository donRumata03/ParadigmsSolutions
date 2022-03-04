package queue;

public class QueueTestMy {

    public static void useQueue(Queue queue) {
        queue.enqueue(10);
        queue.enqueue("Hello");

        System.out.println(queue.indexOf(10) == 1);
        System.out.println(queue.indexOf("Hello") == 0);

        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }

        assert queue.isEmpty();
    }

    public static void simpleTest() {

    }

    public static void main(String[] args) {
        Queue arrayQueue = new ArrayQueue();
        Queue linkedQueue = new LinkedQueue();

        useQueue(arrayQueue);
        useQueue(linkedQueue);


//        queue.enqueue(["do"]);
    }
}
