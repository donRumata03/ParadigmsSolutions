package expression.parser.tests;

import expression.TripleExpression;
import expression.general.exceptions.IntegerOverflowException;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTests {

    public TripleExpression parseFrom(String testCase) {
        return new expression.parser.ExpressionParser().parse(testCase);
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

}
