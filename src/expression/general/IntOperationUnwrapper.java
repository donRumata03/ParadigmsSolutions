package expression.general;

import expression.Expression;
import expression.TripleExpression;


/**
 * Men inherit from it and IntBinaryOperation implements Expression, TripleExpression for them
 * Child should provide which `Origin` — generic type from which it's implemented
 */
public interface IntOperationUnwrapper
    extends GenericExpression<Integer>, GenericTripleExpression<Integer>,
    Expression, TripleExpression
{
    @Override
    default int evaluate(int x) {
        return evaluate(Integer.valueOf(x));
    }

    @Override
    default int evaluate(int x, int y, int z) {
        return evaluate(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
    }
}
