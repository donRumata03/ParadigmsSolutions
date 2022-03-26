package expression;

import expression.general.IntOperationUnwrapper;

public class Variable
    extends expression.general.operations.Variable<Integer>
    implements IntOperationUnwrapper
{

    public Variable(String varName) {
        super(varName);
    }
}
