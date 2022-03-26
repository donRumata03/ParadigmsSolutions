package jstest.object;

import base.Selector;
import jstest.expression.Builder;
import jstest.functional.ExpressionTest;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ObjectTest {
    /* package-private */
    static Selector.Composite<Builder> selector() {
         return Builder.selector(
                ObjectTest.class,
                mode -> false,
                (builder, counter) -> new ObjectTester(
                        counter,
                        builder.aliased(ObjectTester.OBJECT, ExpressionTest.POLISH),
                        "toString", "parse"
                ),
                "easy", "", "hard", "bonus"
        );
    }

    public static final Selector SELECTOR = selector()
            .variant("Base")
            .selector();

    private ObjectTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
