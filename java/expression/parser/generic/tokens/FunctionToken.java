package expression.parser.generic.tokens;


import expression.general.ParenthesesTrackingExpression;

public enum FunctionToken implements AbstractOperationToken {
    l0,
    t0,
    abs,
    min,
    max,
    count;

    @Override
    public boolean canBeUnary() {
        return this != min && this != max;
    }

    @Override
    public boolean canBeBinary() {
        return this == min || this == max;
    }
}
