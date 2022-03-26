package expression.parser.generic.tokens;

public enum OperatorToken implements AbstractOperationToken {
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    SHIFT_LEFT,
    LOGICAL_SHIFT_RIGHT,
    ARITHMETICAL_SHIFT,
    POW,
    LOG;

    @Override
    public boolean canBeUnary() {
        return this == MINUS;
    }

    @Override
    public boolean canBeBinary() {
        return true;
    }

//    @Override
//    public ParenthesesTrackingExpression constructUnaryExpression(ParenthesesTrackingExpression child, boolean checked) {
//        assert canBeUnary();
//
//        return checked ? new CheckedNegate(child) : new Negate(child);
//    }
//
//    @Override
//    public ParenthesesTrackingExpression constructBinaryExpression(ParenthesesTrackingExpression left,
//        ParenthesesTrackingExpression right, boolean checked)
//    {
//        return switch (this) {
//            case PLUS -> checked ? new CheckedAdd(left, right) : new Add(left, right);
//            case MINUS -> checked ? new CheckedSubtract(left, right) : new Subtract(left, right);
//            case MULTIPLY -> checked ? new CheckedMultiply(left, right) : new Multiply(left, right);
//            case DIVIDE -> checked ? new CheckedDivide(left, right) : new Divide(left, right);
//            case SHIFT_LEFT -> new ShiftLeft(left, right);
//            case LOGICAL_SHIFT_RIGHT -> new LogicalShiftRight(left, right);
//            case ARITHMETICAL_SHIFT -> new ArithmeticShiftRight(left, right);
//            case POW -> new Pow(left, right);
//            case LOG -> new Log(left, right);
//        };
//    }


}
