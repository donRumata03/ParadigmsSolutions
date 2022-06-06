package expression.exceptions;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.Abs;

public class CheckedAbs
    extends Abs<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper
{

    public CheckedAbs(ParenthesesTrackingExpression<Integer> child) {
        super(child, new CheckedIntegerArithmetics());
    }
}
