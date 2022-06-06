package expression.exceptions;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.ArithmeticShiftRight;
import expression.general.operations.ShiftLeft;

public class CheckedArithmeticShiftRight
    extends ArithmeticShiftRight<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper
{

    public CheckedArithmeticShiftRight(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new CheckedIntegerArithmetics());
    }
}
