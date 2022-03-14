package expression.generic;

import base.Selector;
import expression.common.Op;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class GenericTest {
    private GenericTest() {
    }

    public static class Mode<T> implements Consumer<GenericTester> {
        private final String mode;
        private final IntFunction<T> constant;
        private final List<Op<UnaryOperator<GenericTester.F<T>>>> unary = new ArrayList<>();
        private final List<Op<BinaryOperator<GenericTester.F<T>>>> binary = new ArrayList<>();
        private final IntUnaryOperator fixer;

        public Mode(final String mode, final IntFunction<T> constant, final IntUnaryOperator fixer) {
            this.mode = mode;
            this.constant = constant;
            this.fixer = fixer;
        }

        public Mode<T> unary(final String name, final UnaryOperator<T> op) {
            unary.add(Op.of(name, arg -> (x, y, z) -> op.apply(arg.apply(x, y, z))));
            return this;
        }

        public Mode<T> binary(final String name, final BinaryOperator<T> op) {
            binary.add(Op.of(name, (a, b) -> (x, y, z) -> op.apply(a.apply(x, y, z), b.apply(x, y, z))));
            return this;
        }

        @Override
        public void accept(final GenericTester tester) {
            tester.mode(mode, constant, unary, binary, fixer);
        }
    }

    private static final Consumer<GenericTester> ADD = binary("+", 200);
    private static final Consumer<GenericTester> SUBTRACT = binary("-", -200);
    private static final Consumer<GenericTester> MULTIPLY = binary("*", 301);
    private static final Consumer<GenericTester> DIVIDE = binary("/", -300);

    private static Consumer<GenericTester> binary(final String name, final int priority) {
        return tester -> tester.binary(name, priority);
    }

    private static Integer i(final long v) {
        if (v != (int) v) {
            throw new ArithmeticException("Overflow");
        }
        return (int) v;
    }

    private static final Mode<Integer> INTEGER_CHECKED = mode("i", c -> c)
            .binary("+", (a, b) -> i(a + (long) b))
            .binary("-", (a, b) -> i(a - (long) b))
            .binary("*", (a, b) -> i(a * (long) b))
            .binary("/", (a, b) -> i(a / (long) b))
            ;

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Double> DOUBLE = mode("d", c -> (double) c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            ;

    private static final Mode<BigInteger> BIG_INTEGER = mode("bi", BigInteger::valueOf)
            .binary("+", BigInteger::add)
            .binary("-", BigInteger::subtract)
            .binary("*", BigInteger::multiply)
            .binary("/", BigInteger::divide)
            ;

    public static final Selector SELECTOR = Selector.composite(GenericTest.class, GenericTester::new, "easy", "hard")
            .variant("Base", INTEGER_CHECKED, DOUBLE, BIG_INTEGER, ADD, SUBTRACT, MULTIPLY, DIVIDE)
            .selector();

    private static <T> Mode<T> mode(final String mode, final IntFunction<T> constant) {
        return new Mode<>(mode, constant, IntUnaryOperator.identity());
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
