package cljtest.linear;

import base.ExtendedRandom;
import base.Selector;
import base.TestCounter;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class LinearTest {
    // Selector
    public static final Selector SELECTOR = new Selector(LinearTester.class, "easy", "hard")
            .variant("Base", v(LinearTester::new))
            ;

    private LinearTest() {
    }

    /* package-private*/ static Consumer<TestCounter> v(final Function<TestCounter, LinearTester> variant) {
        return counter -> variant.apply(counter).test();
    }

    /* package-private*/ static Consumer<TestCounter> variant(final List<Item.Fun> functions, final Consumer<Test> variant) {
        return v(counter -> new LinearTester(counter) {
            @Override
            protected void test(final int args) {
                variant.accept(new Test(this, functions, args));
            }
        });
    }

    /* package-private */ static class Test {
        private final LinearTester test;
        private final List<Item.Fun> functions;
        /* package-private */ final int args;

        public Test(final LinearTester test, final List<Item.Fun> functions, final int args) {
            this.test = test;
            this.functions = functions;
            this.args = args;
        }

        public void test(final Supplier<Item> generator) {
            test.test(args, functions, generator);
        }

        public boolean isHard() {
            return test.isHard();
        }

        public void expectException(final int[] okDims, final int[][] failDims) {
            test.expectException(functions, okDims, failDims);
        }

        public ExtendedRandom random() {
            return test.random();
        }
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
