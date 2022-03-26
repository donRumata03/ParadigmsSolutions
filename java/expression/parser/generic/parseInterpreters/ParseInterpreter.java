package expression.parser.generic.parseInterpreters;

import expression.general.ParenthesesTrackingExpression;
import expression.parser.generic.tokens.AbstractOperationToken;

public interface ParseInterpreter<Element> {
    Element parseSignedInt(String toParse);

    ParenthesesTrackingExpression<Element> constructUnaryExpression(AbstractOperationToken token, ParenthesesTrackingExpression<Element> child);

    ParenthesesTrackingExpression<Element> constructBinaryExpression(
        AbstractOperationToken token, ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right
    );

    ParenthesesTrackingExpression<Element> constructVariable(String name);
    ParenthesesTrackingExpression<Element> constructConst(Element value);
}
