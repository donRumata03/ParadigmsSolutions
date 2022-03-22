package expression.exceptions;

import expression.Expression;
import expression.TripleExpression;
import expression.BigDecimalExpression;
import expression.BigIntegerExpression;
import expression.general.IntBinaryOperation;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.Add;
import expression.general.ParenthesesTrackingExpression;

public class CheckedAdd
    extends Add<Integer, CheckedIntegerArithmetics>
    implements IntBinaryOperation, Expression, TripleExpression
{

    public CheckedAdd(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new CheckedIntegerArithmetics());
    }

    @Override
    public int evaluate(int x) {
        return IntBinaryOperation.super.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return IntBinaryOperation.super.evaluate(x, y, z);
    }
}
