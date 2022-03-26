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
        Queue arrayQueue = new ArrayQueue();
        Queue linkedQueue = new LinkedQueue();

        useQueue(arrayQueue);
        useQueue(linkedQueue);
    }

    public static void testFilteringArrayQueue() {
        Queue q = new ArrayQueue();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);

        q.retainIf((Object o) -> ((int)o) % 2 == 0);

        System.out.println("RemoveD");
    }


    public static void main(String[] args) {
        testFilteringArrayQueue();


//        queue.enqueue(["do"]);
    }
}
