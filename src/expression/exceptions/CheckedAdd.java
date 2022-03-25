package expression.exceptions;

import expression.Expression;
import expression.TripleExpression;
import expression.general.IntOperationUnwrapper;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.Add;
import expression.general.ParenthesesTrackingExpression;

public class CheckedAdd
    extends Add<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper, Expression, TripleExpression
{

    public CheckedAdd(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new CheckedIntegerArithmetics());
    }

    @Override
    public int evaluate(int x) {
        return IntOperationUnwrapper.super.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return IntOperationUnwrapper.super.evaluate(x, y, z);
    }
}
