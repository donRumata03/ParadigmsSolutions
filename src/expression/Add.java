package expression;

import expression.general.arithmetics.BigIntegerArithmetics;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import java.math.BigInteger;

/**
 * Add should implement expression interfaces: Expression, Triple expression, …
 */
public class Add
    extends expression.general.operations.Add<BigInteger, BigIntegerArithmetics>
    implements BigIntegerExpression
{

//    public Add integerAdd(
//        ParenthesesTrackingExpression<Integer, UncheckedIntegerArithmetics> left,
//        ParenthesesTrackingExpression<Integer, UncheckedIntegerArithmetics> right
//    ) {
//
//    }

    // TODO: resolve somehow
    public Add(
        ParenthesesTrackingExpression<BigInteger, BigIntegerArithmetics> left,
        ParenthesesTrackingExpression<BigInteger, BigIntegerArithmetics> right
    ) {
        super(left, right, new BigIntegerArithmetics());


    }

    public Add(
        ParenthesesTrackingExpression<Integer, UncheckedIntegerArithmetics> left,
        ParenthesesTrackingExpression<Integer, UncheckedIntegerArithmetics> right
    ) {
        super(left, right, new UncheckedIntegerArithmetics());


    }
}
