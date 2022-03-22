package expression;

import expression.general.IntBinaryOperation;
import expression.general.arithmetics.BigIntegerArithmetics;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import java.math.BigInteger;

/**
 * Add should implement expression interfaces: Expression, Triple expression, BigIntegerExpression, BigDecimalExpression
 * (integer are unchecked ones)
 */
public class Add
    extends expression.general.operations.Add<Integer, UncheckedIntegerArithmetics>
    implements IntBinaryOperation, Expression, TripleExpression, BigIntegerExpression
{

//    public Add integerAdd(
//        ParenthesesTrackingExpression<Integer, UncheckedIntegerArithmetics> left,
//        ParenthesesTrackingExpression<Integer, UncheckedIntegerArithmetics> right
//    ) {
//
//    }

    public Add(
        ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right
    ) {
        super(left, right, new UncheckedIntegerArithmetics());
    }


    @Override
    public BigInteger evaluate(BigInteger x) {
        // TODO: Special arithmetics type that supports both Integer and BigDecimal operations
        // TODO: And special layer that implements specific `evaluate`s through generic one
        throw new UnsupportedOperationException();
    }

    @Override
    public int evaluate(int x) {
        return IntBinaryOperation.super.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return IntBinaryOperation.super.evaluate(x, y, z);
    }
}
