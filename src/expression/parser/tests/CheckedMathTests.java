package expression.parser.tests;

import expression.exceptions.CheckedIntMath;
import org.junit.Assert;
import org.junit.Test;

public class CheckedMathTests {

    @Test
    public void logTest() {
        Assert.assertEquals(0, CheckedIntMath.checkedLog(1, 4));
    }
}
