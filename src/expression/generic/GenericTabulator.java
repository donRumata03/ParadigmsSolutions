package expression.generic;

import expression.Expression;
import expression.TripleExpression;
import expression.general.exceptions.ExpressionArithmeticException;

public class GenericTabulator implements Tabulator {

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)
        throws Exception {

        // Use designated dispatcher for choosing computation type based on mode string

        // Build Expression
        ExpressionModeSelector builtExpression = new ExpressionModeSelector(expression, mode); // If we can't build, let exception unwind up the stack

        Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Object cellRes;
                    try {
                        cellRes = builtExpression.compute(x, y, z);
                    } catch (ExpressionArithmeticException e) {
                        cellRes = null;
                    }

                    res[x - x1][y - y1][z - z1] = cellRes;
                }
            }
        }

        return res;
    }
}
