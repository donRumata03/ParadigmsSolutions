package expression.exceptions;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.Add;
import expression.general.operations.Divide;

public class CheckedDivide
    extends Divide<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper
{

    public CheckedDivide(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new CheckedIntegerArithmetics());
    }
}
