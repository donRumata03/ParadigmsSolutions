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
    private static final Consumer<GenericTester> COUNT = unary("count");
    private static final Consumer<GenericTester> MIN = binary("min", 50);
    private static final Consumer<GenericTester> MAX = binary("max", 50);

    private static final int TRUNCATE = 10;
    private static int truncate(final int v) {
        return v / TRUNCATE * TRUNCATE;
    }
    private static final Mode<Integer> INTEGER_TRUNCATE = mode("t", GenericTest::truncate)
            .binary("+", (a, b) -> truncate(a + b))
            .binary("-", (a, b) -> truncate(a - b))
            .binary("*", (a, b) -> truncate(a * b))
            .binary("/", (a, b) -> truncate(a / b))
            .unary("abs", Math::abs)
            .unary("square", a -> truncate(a * a))
            .binary("mod", (a, b) -> truncate(a % b))
            .unary("count", a -> truncate(Integer.bitCount(a)))
            .binary("min", Math::min)
            .binary("max", Math::max)
            ;

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Integer> INTEGER_UNCHECKED = mode("u", c -> c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("abs", Math::abs)
            .unary("square", a -> a * a)
            .binary("mod", (a, b) -> a % b)
            .unary("count", Integer::bitCount)
            .binary("min", Math::min)
            .binary("max", Math::max)
            ;

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Long> LONG = mode("l", c -> (long) c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("abs", Math::abs)
            .unary("abs", a -> Math.abs(a))
            .unary("square", a -> a * a)
            .binary("mod", (a, b) -> a % b)
            .unary("count", i -> (long) Long.bitCount(i))
            .binary("min", Math::min)
            .binary("max", Math::max)
            ;

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Float> FLOAT = mode("f", c -> (float) c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("abs", a -> Math.abs(a))
            .unary("square", a -> a * a)
            .binary("mod", (a, b) -> a % b)
            .unary("count", a -> (float) Integer.bitCount(Float.floatToIntBits(a)))
            .binary("min", Math::min)
            .binary("max", Math::max)
            ;


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

    private static Consumer<GenericTester> unary(final String name) {
        return tester -> tester.unary(name);
    }

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
            .unary("count", Integer::bitCount)
            .binary("min", Math::min)
            .binary("max", Math::max)
            ;

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Double> DOUBLE = mode("d", c -> (double) c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("count", a -> (double) Long.bitCount(Double.doubleToLongBits(a)))
            .binary("min", Math::min)
            .binary("max", Math::max)
            ;

    private static final Mode<BigInteger> BIG_INTEGER = mode("bi", BigInteger::valueOf)
            .binary("+", BigInteger::add)
            .binary("-", BigInteger::subtract)
            .binary("*", BigInteger::multiply)
            .binary("/", BigInteger::divide)
            .unary("count", a -> BigInteger.valueOf(a.bitCount()))
            .binary("min", BigInteger::min)
            .binary("max", BigInteger::max)
            ;

    private static short s(final int x) {
        return (short) x;
    }

    private static final Mode<Short> SHORT = mode("s", c -> (short) c)
            .binary("+", (a, b) -> s(a + b))
            .binary("-", (a, b) -> s(a - b))
            .binary("*", (a, b) -> s(a * b))
            .binary("/", (a, b) -> s(a / b))
            .unary("count", a -> s(Integer.bitCount(a & 0xffff)))
            .binary("min", (a, b) -> s(Math.min(a, b)))
            .binary("max", (a, b) -> s(Math.max(a, b)))
            ;

    public static final Selector SELECTOR = Selector.composite(GenericTest.class, GenericTester::new, "easy", "hard")
            .variant("Base", INTEGER_CHECKED, DOUBLE, BIG_INTEGER, ADD, SUBTRACT, MULTIPLY, DIVIDE)
            .variant("Cmm", COUNT, MIN, MAX)
            .variant("CmmUls", COUNT, MIN, MAX, INTEGER_UNCHECKED, LONG, SHORT)
            .variant("CmmUlf", COUNT, MIN, MAX, INTEGER_UNCHECKED, LONG, FLOAT)
            .variant("CmmUlt", COUNT, MIN, MAX, INTEGER_UNCHECKED, LONG, INTEGER_TRUNCATE)
            .selector();

    private static <T> Mode<T> mode(final String mode, final IntFunction<T> constant) {
        return new Mode<>(mode, constant, IntUnaryOperator.identity());
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
