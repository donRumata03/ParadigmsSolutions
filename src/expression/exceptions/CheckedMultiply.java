package expression.exceptions;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.Divide;
import expression.general.operations.Multiply;

public class CheckedMultiply
    extends Multiply<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper
{

    public CheckedMultiply(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new CheckedIntegerArithmetics());
    }
}
