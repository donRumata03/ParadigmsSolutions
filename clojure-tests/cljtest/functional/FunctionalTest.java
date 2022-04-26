package cljtest.functional;

import base.Selector;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class FunctionalTest {
    private static final Selector SELECTOR = FunctionalTester.builder()
            .variant("Base",            ARITH)
            .variant("MeanVarn",        ARITH, MEAN,        VARN)
            .selector();

    private FunctionalTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
