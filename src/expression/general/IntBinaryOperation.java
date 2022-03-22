package expression.general;

import expression.BigDecimalExpression;
import expression.Expression;
import expression.TripleExpression;
import expression.general.arithmetics.ArithmeticEngine;


/**
 * Men inherit from it and IntBinaryOperation implements Expression, TripleExpression for them
 * Child should provide which `Origin` — generic type from which it's implemented
 */
public interface IntBinaryOperation
    extends GenericExpression<Integer>, GenericTripleExpression<Integer>
{
    default int evaluate(int x) {
        return evaluate(Integer.valueOf(x));
    }

    default int evaluate(int x, int y, int z) {
        return evaluate(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
    }
}
