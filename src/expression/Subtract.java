package expression;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.UncheckedIntegerArithmetics;

public class Subtract
    extends expression.general.operations.Subtract<Integer, UncheckedIntegerArithmetics>
    implements IntOperationUnwrapper, BigDecimalOperationDiscarder
{
    public Subtract(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new UncheckedIntegerArithmetics());
    }
}
