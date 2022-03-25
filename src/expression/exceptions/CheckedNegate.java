package expression.exceptions;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.Multiply;
import expression.general.operations.Negate;

public class CheckedNegate
    extends Negate<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper
{

    public CheckedNegate(ParenthesesTrackingExpression<Integer> child) {
        super(child, new CheckedIntegerArithmetics());
    }
}
