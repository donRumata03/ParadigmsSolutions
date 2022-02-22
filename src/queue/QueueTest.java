package queue;

import base.Selector;
import base.TestCounter;
import queue.Queues.QueueChecker;
import queue.Queues.QueueModel;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class QueueTest {
    public static final Selector<?> SELECTOR = Selector.<Consumer<TestCounter>>create(QueueTest.class)
            .variant("Base", variant(QueueModel.class, d -> () -> d))
            ;

    private QueueTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }

    /* package-private */ static <M extends QueueModel, T extends QueueChecker<M>> Consumer<TestCounter> variant(
            final Class<M> type,
            final T tester,
            final Queues.Splitter<M> splitter
    ) {
        return new QueueTester<>(type, tester, splitter)::test;
    }

    /* package-private */ public static <M extends QueueModel, T extends QueueChecker<M>> Consumer<TestCounter> variant(
            final Class<M> type,
            final T tester
    ) {
        return variant(type, tester, (t, q, r) -> List.of());
    }
}
