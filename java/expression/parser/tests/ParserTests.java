package expression.parser.tests;

import expression.TripleExpression;
import expression.general.GenericTripleExpression;
import expression.general.arithmetics.ArithmeticEngine;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.arithmetics.UncheckedDoubleArithmetics;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import expression.general.exceptions.IntegerOverflowException;
import java.io.IOException;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

public class ParserTests {

    public TripleExpression parseFrom(String testCase) {
        return new expression.parser.ExpressionParser().parse(testCase);
    }

    public <T, Engine extends ArithmeticEngine<T>> GenericTripleExpression<T> parseGenericFrom(String testCase, Engine engine) {
        return new expression.generic.GenericParser<>(engine).parse(testCase);
    }

    public TripleExpression parseCheckedFrom(String testCase) {
        return new expression.exceptions.ExpressionParser().parse(testCase);
    }

    @Test
    public void testFactor() throws IOException {
        var t1 = parseFrom("-142490");
        var t2 = parseFrom("2342134");
        var t3 = parseFrom("    -   - -  - -2342134");
        var t4 = parseFrom("  t0  -   - -  - l0 -2342134");
        var t5 = parseFrom("0 >> 0");
//        ExpectException parseFactorFrom("*");
    }

    @Test
    public void testParentheses() throws IOException {
        var t1 = parseFrom("x*y+(z-1   )/10");
//        Assert.assertEquals("-(x + y)", t1.toMiniString());
    }

    @Test
    public void testWithToMiniString() {
        var z = parseFrom("- 0");
        Assert.assertEquals("- 0", z.toMiniString());
    }

    @Test
    public void testChecked() {
        var e = parseCheckedFrom("-(-2147483648)");
        Assert.assertThrows(IntegerOverflowException.class,  () -> e.evaluate(0, 0, 0));
    }


    @Test
    public void testPow() {
        var e = parseCheckedFrom("(0 ** 1)");
        Assert.assertEquals(0,  e.evaluate(0, 0, 0));
    }

    @Test
    public void powToMiniStingTest() {
        var e = parseCheckedFrom("(y ** (x ** x))");
        Assert.assertEquals("y ** (x ** x)",  e.toMiniString());
    }

    @Test
    public void macOSProblem() {
        var e = parseCheckedFrom("    (2147483647 / 10)");
    }

    @Test
    public void parseMin() {
        var e = parseGenericFrom("-100 min 4", new CheckedIntegerArithmetics());
        Assert.assertEquals(Integer.valueOf(-100),  e.evaluate(1, 2, 3));
    }

    @Test
    public void parseBigNumber() {
        var e = parseCheckedFrom("(((z + y) - 2147483647) + (y + y))");
        System.out.println(e.toMiniString());
    }

    @Test
    public void parseSmallNumber() {
        var e = parseCheckedFrom("(((z + y) - 1) + (y + y))");
        System.out.println(e.toMiniString());
    }

    @Test
    public void parseSmallNumberWithPlus() {
        var e = parseCheckedFrom("(((z + y) + 1) + (y + y))");
        System.out.println(e.toMiniString());
    }

    @Test
    public void parseBigNumberReduced() {
        var e = parseCheckedFrom("(z + y) - 2147483647");
        System.out.println(e.toMiniString());
    }

    @Test
    public void parseBigNumberUnchecked() {
        var e = parseGenericFrom("(((z + y) - 2147483647) + (y + y))", new UncheckedIntegerArithmetics());
        System.out.println(e.toString());
    }

    @Test
    public void parseUnaryMinusChecked() {
        var e = parseCheckedFrom("- 1");
        System.out.println(e.toString());
    }

    @Test
    public void parseBinaryMinusChecked() {
        var e = parseCheckedFrom("x - 1");
        System.out.println(e.toString());
    }
}
