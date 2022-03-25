package expression.exceptions;

import expression.BigDecimalOperationDiscarder;
import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.Add;
import expression.general.operations.Subtract;

public class CheckedSubtract
    extends Subtract<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper, BigDecimalOperationDiscarder
{

    public CheckedSubtract(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new CheckedIntegerArithmetics());
    }
}
