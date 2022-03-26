package expression.general;

import expression.general.arithmetics.ArithmeticEngine;

public abstract class AtomicParenthesesTrackingExpression<T> extends ParenthesesTrackingExpression<T> {
    @Override
    public ParenthesesElisionTrackingInfo getCachedPriorityInfo() {
        return ParenthesesElisionTrackingInfo.generateAtomicExpressionInfo();
    }
    @Override
    public DummyParenthesesElisionTrackingInfo getDummyCachedPriorityInfo() {
        return DummyParenthesesElisionTrackingInfo.generateAtomicExpressionInfo();
    }

    @Override
    public void resetCachedPriorityInfo() {}

    @Override
    public void toMiniStringBuilderCorrect(StringBuilder builder) {
        toStringBuilder(builder);
    }
}
