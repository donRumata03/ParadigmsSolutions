package expression.general;

public abstract class AtomicParenthesesTrackingExpression extends ParenthesesTrackingExpression {
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
