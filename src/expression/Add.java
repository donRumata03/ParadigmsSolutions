package expression;

import expression.general.arithmetics.BigIntegerArithmetics;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import java.math.BigInteger;

/**
 * Add should implement expression interfaces: Expression, Triple expression, …
 */
public class Add
//    extends expression.general.operations.Add<BigInteger, BigIntegerArithmetics>
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
        ParenthesesTrackingExpression<?, ?> left,
        ParenthesesTrackingExpression<?, ?> right
    ) {
        super(left, right);


    }

    @Override
    public BigInteger reductionOperation(BigInteger left, BigInteger right) {
        return new BigIntegerArithmetics().add(left, right);
    }


    @Override
    public Integer reductionOperation(Integer left, Integer right) {
        return new UncheckedIntegerArithmetics().add(left, right);
    }
}
