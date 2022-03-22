package expression.parser.generic.tokens;

import expression.general.ParenthesesTrackingExpression;

public interface AbstractOperationToken extends ArithmeticExpressionToken {
    boolean canBeUnary();
    boolean canBeBinary();
}
