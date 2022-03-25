package expression;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.UncheckedIntegerArithmetics;

public class Divide extends expression.general.operations.Divide<Integer, UncheckedIntegerArithmetics>
    implements IntOperationUnwrapper, BigDecimalOperationDiscarder
{

    public Divide(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new UncheckedIntegerArithmetics());
    }
}
