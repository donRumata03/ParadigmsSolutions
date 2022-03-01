package queue;

import base.ExtendedRandom;
import base.Selector;
import base.TestCounter;
import queue.Queues.QueueChecker;
import queue.Queues.QueueModel;

import java.util.ArrayDeque;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class QueueTest {
    // === Functions

    /* package-private */  interface FunctionsModel extends QueueModel {
        @ReflectionTest.Wrap
        default FunctionsModel filter(final Predicate<Object> p) {
            return apply(Stream::filter, p);
        }

        @ReflectionTest.Wrap
        default FunctionsModel map(final Function<Object, Object> f) {
            return apply(Stream::map, f);
        }

        private <T> FunctionsModel apply(final BiFunction<Stream<Object>, T, Stream<Object>> f, final T v) {
            final ArrayDeque<Object> deque = f.apply(model().stream(), v).collect(Collectors.toCollection(ArrayDeque::new));
            return () -> deque;
        }
    }

    /* package-private */ static final Queues.Splitter<FunctionsModel> FUNCTIONS = (tester, queue, random) -> {
        final FunctionsModel result = random.nextBoolean()
                                                 ? queue.filter(Predicate.isEqual(tester.randomElement(random)))
                                                 : queue.map(random.nextBoolean() ? String::valueOf : Object::hashCode);
        return List.of(tester.cast(result));
    };

    // === If

    /* package-private */ interface IfModel extends QueueModel {
        default void removeIf(final Predicate<Object> p) {
            model().removeIf(p);
        }

        default void retainIf(final Predicate<Object> p) {
            model().removeIf(Predicate.not(p));
        }
    }

    /* package-private */ static Predicate<Object> randomPredicate(final QueueChecker<? extends QueueModel> tester, final ExtendedRandom random) {
        return new Predicate<>() {
            final Object element = tester.randomElement(random);

            @Override
            public boolean test(final Object o) {
                return o == element;
            }

            @Override
            public String toString() {
                return "== " + element;
            }
        };
    }

    /* package-private */ static final Queues.LinearTester<IfModel> IF = (tester, queue, random) -> {
        if (random.nextBoolean()) {
            queue.removeIf(randomPredicate(tester, random));
        } else {
            queue.retainIf(randomPredicate(tester, random));
        }
    };


    // === While

    /* package-private */ interface WhileModel extends QueueModel {
        // Deliberately ugly implementation
        default void dropWhile(final Predicate<Object> p) {
            final boolean[] remove = {true};
            model().removeIf(e -> remove[0] &= p.test(e));
        }

        // Deliberately ugly implementation
        default void takeWhile(final Predicate<Object> p) {
            final boolean[] keep = {true};
            model().removeIf(e -> !(keep[0] &= p.test(e)));
        }
    }

    /* package-private */ static final Queues.LinearTester<WhileModel> WHILE = (tester, queue, random) -> {
        if (random.nextBoolean()) {
            queue.takeWhile(randomPredicate(tester, random));
        } else {
            queue.dropWhile(randomPredicate(tester, random));
        }
    };


    /* package-private */ interface IfWhileModel extends IfModel, WhileModel {
    }

    /* package-private */ static final Queues.LinearTester<IfWhileModel> IF_WHILE = (tester, queue, random) -> {
        final Queues.LinearTester<IfWhileModel> t = random.nextBoolean() ? IF::test : WHILE::test;
        t.test(tester, queue, random);
    };


    public static final Selector SELECTOR = new Selector(QueueTest.class)
            .variant("Base", variant(QueueModel.class, d -> () -> d))
            .variant("Functions", variant(FunctionsModel.class, d -> () -> d, FUNCTIONS))
            .variant("IfWhile", variant(IfWhileModel.class, d -> () -> d, IF_WHILE))
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
