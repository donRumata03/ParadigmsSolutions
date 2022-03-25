package expression;

import expression.general.IntOperationUnwrapper;
import expression.general.arithmetics.UncheckedIntegerArithmetics;

public class Const
    extends expression.general.operations.Const<Integer>
    implements IntOperationUnwrapper, BigDecimalOperationDiscarder
{
    public Const(int value) {
        super(value);
    }
}
