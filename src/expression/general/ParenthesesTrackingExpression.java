package expression.general;

/**
 * An expression is responsible for adding parentheses TO ITS CHILDREN if necessary (it knows better, when)
 */
public abstract class ParenthesesTrackingExpression extends StringBuildableExpression {
    abstract public ParenthesesElisionTrackingInfo getCachedPriorityInfo();
    abstract public DummyParenthesesElisionTrackingInfo getDummyCachedPriorityInfo();
    abstract public void resetCachedPriorityInfo();
}
