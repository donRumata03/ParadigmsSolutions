package queue;

import base.ExtendedRandom;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Queues {
    /* package-private */ interface QueueModel {
        @ReflectionTest.Ignore
        ArrayDeque<Object> model();

        default Object dequeue() {
            return model().removeFirst();
        }

        default int size() {
            return model().size();
        }

        default boolean isEmpty() {
            return model().isEmpty();
        }

        default void clear() {
            model().clear();
        }

        default void enqueue(final Object element) {
            model().addLast(element);
        }

        default Object element() {
            return model().getFirst();
        }
    }

    /* package-private */ interface QueueChecker<T extends QueueModel> {
        T wrap(ArrayDeque<Object> reference);

        default List<T> linearTest(final T queue, final ExtendedRandom random) {
            // Do nothing by default
            return List.of();
        }

        default void check(final T queue, final ExtendedRandom random) {
            queue.element();
        }

        default void add(final T queue, final Object element, final ExtendedRandom random) {
            queue.enqueue(element);
        }

        default Object randomElement(final ExtendedRandom random) {
            return ArrayQueueTester.ELEMENTS[random.nextInt(ArrayQueueTester.ELEMENTS.length)];
        }

        default void remove(final T queue, final ExtendedRandom random) {
            queue.dequeue();
        }

        @SuppressWarnings("unchecked")
        default T cast(final QueueModel model) {
            return (T) model;
        }
    }

    @FunctionalInterface
    protected interface Splitter<M extends QueueModel> {
        List<M> split(final QueueChecker<? extends M> tester, final M queue, final ExtendedRandom random);
    }

    @FunctionalInterface
    protected interface LinearTester<M extends QueueModel> extends Splitter<M> {
        void test(final QueueChecker<? extends M> tester, final M queue, final ExtendedRandom random);

        @Override
        default List<M> split(final QueueChecker<? extends M> tester, final M queue, final ExtendedRandom random) {
            test(tester, queue, random);
            return List.of();
        }
    }
    /* package-private */ interface DequeModel extends QueueModel {
        default void push(final Object element) {
            model().addFirst(element);
        }

        @SuppressWarnings("UnusedReturnValue")
        default Object peek() {
            return model().getLast();
        }

        default Object remove() {
            return model().removeLast();
        }
    }

    /* package-private */ interface DequeChecker<T extends DequeModel> extends QueueChecker<T> {
        @Override
        default void add(final T queue, final Object element, final ExtendedRandom random) {
            if (random.nextBoolean()) {
                QueueChecker.super.add(queue, element, random);
            } else {
                queue.push(element);
            }
        }

        @Override
        default void check(final T queue, final ExtendedRandom random) {
            if (random.nextBoolean()) {
                QueueChecker.super.check(queue, random);
            } else {
                queue.peek();
            }
        }

        @Override
        default void remove(final T queue, final ExtendedRandom random) {
            if (random.nextBoolean()) {
                QueueChecker.super.remove(queue, random);
            } else {
                queue.remove();
            }
        }
    }

    // === Reflection

    /* package-private */ interface ReflectionModel extends QueueModel {
        Field ELEMENTS = getField("elements");
        Field HEAD = getField("head");

        @SuppressWarnings("unchecked")
        private <Z> Z get(final Field field) {
            try {
                return (Z) field.get(model());
            } catch (final IllegalAccessException e) {
                throw new AssertionError("Cannot access field " + field.getName() + ": " + e.getMessage(), e);
            }
        }

        private static Field getField(final String name) {
            try {
                final Field field = ArrayDeque.class.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (final NoSuchFieldException e) {
                throw new AssertionError("Reflection error: " + e.getMessage(), e);
            }
        }

        @ReflectionTest.Ignore
        default int head() {
            return get(HEAD);
        }

        @ReflectionTest.Ignore
        default Object[] elements() {
            return get(ELEMENTS);
        }

        @ReflectionTest.Ignore
        default <R> R reduce(final R zero, final Predicate<Object> p, final BiFunction<R, Integer, R> f) {
            final int size = size();
            final Object[] elements = elements();
            final int head = head();
            R result = zero;
            for (int i = 0; i < size; i++) {
                if (p.test(elements[(head + i) % elements.length])) {
                    result = f.apply(result, i);
                }
            }
            return result;
        }

        @ReflectionTest.Ignore
        default <R> R reduce(final R zero, final Object v, final BiFunction<R, Integer, R> f) {
            return reduce(zero, o -> Objects.equals(v, o), f);
        }
    }


    // === Count

    /* package-private */ interface CountModel extends ReflectionModel {
        default int count(final Object element) {
            return reduce(0, element, (v, i) -> v + 1);
        }
    }

    /* package-private */ static final LinearTester<CountModel> COUNT =
            (tester, queue, random) -> queue.count(tester.randomElement(random));

    // === DequeCount

    /* package-private */ interface DequeCountModel extends DequeModel, CountModel {
    }

    /* package-private */ static final LinearTester<DequeCountModel> DEQUE_COUNT = COUNT::test;

    // === Index

    /* package-private */
    interface IndexModel extends ReflectionModel {
        default int indexOf(final Object element) {
            return reduce(-1, element, (v, i) -> v == -1 ? i : v);
        }

        default int lastIndexOf(final Object element) {
            return reduce(-1, element, (v, i) -> i);
        }
    }

    /* package-private */ static final LinearTester<IndexModel> INDEX = (tester, queue, random) -> {
        if (random.nextBoolean()) {
            queue.indexOf(tester.randomElement(random));
        } else {
            queue.lastIndexOf(tester.randomElement(random));
        }
    };


    // === DequeIndex

    /* package-private */ interface DequeIndexModel extends DequeModel, IndexModel {
    }

    /* package-private */ static final LinearTester<DequeIndexModel> DEQUE_INDEX = INDEX::test;
}
