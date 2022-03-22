package expression.parser.generic.parseInterpreters;

import expression.general.ParenthesesTrackingExpression;
import expression.parser.generic.tokens.AbstractOperationToken;

public class MaybeCheckedParseInterpreter implements ParseInterpreter<Integer> {

    @Override
    public Integer parseSignedInt(String toParse) {
        return Integer.parseInt(toParse);
    }

    @Override
    public ParenthesesTrackingExpression<Integer> constructUnaryExpression(AbstractOperationToken token, ParenthesesTrackingExpression<Integer> child) {

    }

    @Override
    public ParenthesesTrackingExpression<Integer> constructBinaryExpression(
        AbstractOperationToken token, ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }
}
