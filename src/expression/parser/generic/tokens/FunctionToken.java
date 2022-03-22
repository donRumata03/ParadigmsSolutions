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

    @Override
    public ParenthesesTrackingExpression constructUnaryExpression(ParenthesesTrackingExpression child, boolean checked) {
        switch (this) {
            case l0 -> {
                return new LeadingZeroes(child);
            }
            case t0 -> {
                return new TrailingZeroes(child);
            }
            case abs -> {
                return new Abs(child);
            }
            default -> throw new RuntimeException("Unknown token => Parser has a programmer's error…");
        }
    }

    @Override
    public ParenthesesTrackingExpression constructBinaryExpression(ParenthesesTrackingExpression left,
        ParenthesesTrackingExpression right, boolean checked)
    {
        throw new RuntimeException();
    }
}
