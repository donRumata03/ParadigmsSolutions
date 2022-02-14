package search;

import base.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BinarySearchTester {
    public static final int[] SIZES = {5, 4, 2, 1, 10, 100, 300};
    public static final int[] VALUES = new int[]{5, 4, 2, 1, 0, 10, 100, Integer.MAX_VALUE / 2};

    private final String className;
    private final Consumer<Variant> variant;

    public BinarySearchTester(final String className, final Consumer<Variant> variant) {
        this.className = "search.BinarySearch" + className;
        this.variant = variant;
    }

    @Override
    public String toString() {
        return className;
    }

    protected void test(final TestCounter counter) {
        variant.accept(new Variant(counter, new MainChecker(Runner.args(className))));
    }

    protected static <T> Consumer<VariantTester<BinarySearchTester>> variant(final BiConsumer<T, Variant> test, final String name, final T value) {
        return VariantTester.variant(new BinarySearchTester(name, tester -> test.accept(value, tester)));
    }

    public static class Variant {
        public final ExtendedRandom random = new ExtendedRandom();
        private final TestCounter counter;
        private final MainChecker checker;

        public Variant(final TestCounter counter, final MainChecker checker) {
            this.counter = counter;
            this.checker = checker;
        }

        void test(final IntStream ints, final String expected) {
            final List<String> input = ints.mapToObj(Integer::toString).collect(Collectors.toUnmodifiableList());
            checker.testEquals(counter, input, List.of(expected));
        }

        public void test(final int expected, final int... a) {
            test(Arrays.stream(a), Integer.toString(expected));
        }
    }

    public static class Sampler {
        private final boolean asc;
        private final boolean dups;
        private final boolean zero;

        public Sampler(final boolean asc, final boolean dups, final boolean zero) {
            this.asc = asc;
            this.dups = dups;
            this.zero = zero;
        }

        public int[] sample(final Variant variant, final int size, final int max) {
            final IntStream sorted = variant.random.getRandom().ints(zero ? size : Math.max(size, 1), -max, max + 1).sorted();
            final int[] ints = (dups ? sorted : sorted.distinct()).toArray();
            if (!asc) {
                final int sz = ints.length;
                for (int i = 0; i < sz / 2; i++) {
                    final int t = ints[i];
                    ints[i] = ints[sz - i - 1];
                    ints[sz - i - 1] = t;
                }
            }
            return ints;
        }
    }
}
