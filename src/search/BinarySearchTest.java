package search;

import base.*;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BinarySearchTest {
    /* package-private */ static long[] base(final int c, final int x, final int[] a) {
        for (int i = 0; i < a.length; i++) {
            if (Integer.compare(a[i], x) != c) {
                return longs(i);
            }
        }
        return longs(a.length);
    }

    public static final ModelessSelector<?> SELECTOR = VariantTester.selector(BinarySearchTest.class, BinarySearchTester::test)
        .variant("Base", Solver.variant("", false, BinarySearchTest::base))
        ;

    public static long[] longs(final long... longs) {
        return longs;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }

    interface Solver {
        static Consumer<VariantTester<BinarySearchTester>> variant(
            final String name,
            final boolean asc,
            final Solver solver
        ) {
            return BinarySearchTester.variant((s, v) -> s.test(new BinarySearchTester.Sampler(asc, true, true), asc ? - 1 : 1, v), name, solver);
        }

        long[] solve(final int c, final int x, final int... a);

        default void test(final BinarySearchTester.Variant variant, final int c, final int x, final int... a) {
            final String expected = Arrays.stream(solve(c, x, a))
                .mapToObj(Long::toString)
                .collect(Collectors.joining(" "));
            variant.test(IntStream.concat(IntStream.of(x), IntStream.of(a)), expected);
        }

        default void test(final BinarySearchTester.Sampler sampler, final int c, final BinarySearchTester.Variant variant) {
            test(variant, c, 0);
            test(variant, c, 0, 0);
            for (final int s : BinarySearchTester.SIZES) {
                final int size = s > 3 * TestCounter.DENOMINATOR ? s / TestCounter.DENOMINATOR : s;
                for (final int max : BinarySearchTester.VALUES) {
                    final int[] a = sampler.sample(variant, size, max);
                    for (int i = 0; i < size; i++) {
                        test(variant, c, a[i], a);
                        if (i != 0) {
                            test(variant, c, (a[i - 1] + a[i]) / 2, a);
                        }
                    }
                    test(variant, c, Integer.MIN_VALUE, a);
                    test(variant, c, Integer.MAX_VALUE, a);
                }
            }
        }
    }
}