package expression.general;

import expression.BigDecimalExpression;
import expression.Expression;
import expression.ToMiniString;
import expression.TripleExpression;
import java.math.BigDecimal;

public class SafestParenthesesTrackingExpressionWrapper extends ParenthesesTrackingExpression {

    private final ToMiniString inner;

    SafestParenthesesTrackingExpressionWrapper(Expression wrapped) {
        this.inner = wrapped;
    }
    SafestParenthesesTrackingExpressionWrapper(TripleExpression wrapped) {
        this.inner = wrapped;
    }


    @Override
    public ParenthesesElisionTrackingInfo getCachedPriorityInfo() {
        return ParenthesesElisionTrackingInfo.generateSafestExpressionInfo();
    }
    @Override
    public DummyParenthesesElisionTrackingInfo getDummyCachedPriorityInfo() {
        return DummyParenthesesElisionTrackingInfo.generateSafestExpressionInfo();
    }


    @Override
    public void resetCachedPriorityInfo() {}

    @Override
    public void toStringBuilder(StringBuilder builder) {
        builder.append(inner.toString());
    }

    @Override
    public void toMiniStringBuilderCorrect(StringBuilder builder) {
        builder.append(inner.toMiniString());
    }

    @Override
    public int evaluate(int x) {
        if (!(inner instanceof Expression expression)) {
            throw new AssertionError("Single-argument expression is only supported by Expression");
        }
        return expression.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (!(inner instanceof TripleExpression tripleExpression)) {
            throw new AssertionError(
                "Three-argument expression is only supported by TripleExpression"
            );
        }
        return tripleExpression.evaluate(x, y, z);
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        if (!(inner instanceof BigDecimalExpression bigDecimalExpression)) {
            throw new AssertionError(
                "BigDecimal-argument expression is only supported by BigDecimalExpression"
            );
        }
        return bigDecimalExpression.evaluate(x);
    }
}
