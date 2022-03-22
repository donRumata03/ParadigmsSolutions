package expression.parser.generic;

import expression.general.ParenthesesTrackingExpression;
import expression.parser.generic.tokens.AbstractOperationToken;

public interface ParseInterpreter<Element> {
    Element parseSignedInt(String toParse);

    ParenthesesTrackingExpression<Element> constructUnaryExpression(AbstractOperationToken token, ParenthesesTrackingExpression<Element> child);

    ParenthesesTrackingExpression<Element> constructBinaryExpression(
        ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right
    );
}
