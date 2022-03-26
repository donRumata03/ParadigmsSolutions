package expression.parser.generic.tokens;


import expression.general.ParenthesesTrackingExpression;

public enum FunctionToken implements AbstractOperationToken {
    l0,
    t0,
    abs;

    @Override
    public boolean canBeUnary() {
        return true;
    }

    @Override
    public boolean canBeBinary() {
        return false;
    }
}
