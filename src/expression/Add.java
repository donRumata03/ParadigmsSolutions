package expression;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Add should implement expression interfaces: Expression, Triple expression, BigIntegerExpression, BigDecimalExpression
 * (integer are unchecked ones)
 */
public class Add
    extends expression.general.operations.Add<Integer, UncheckedIntegerArithmetics>
    implements IntOperationUnwrapper, BigDecimalOperationDiscarder
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
    public BigDecimal evaluate(BigDecimal x) {
        // TODO: Special arithmetics type that supports both Integer and BigDecimal operations
        // TODO: And special layer that implements specific `evaluate`s through generic one
        throw new UnsupportedOperationException();
    }

//    @Override
//    public int evaluate(int x) {
//        return IntOperationUnwrapper.super.evaluate(x);
//    }
//
//    @Override
//    public int evaluate(int x, int y, int z) {
//        return IntOperationUnwrapper.super.evaluate(x, y, z);
//    }
}
