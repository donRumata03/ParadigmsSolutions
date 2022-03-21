package expression.general;

import expression.general.arithmetics.ArithmeticEngine;

/**
 * An expression is responsible for adding parentheses TO ITS CHILDREN if necessary (it knows better, when)
 */
public abstract class ParenthesesTrackingExpression<T, Engine extends ArithmeticEngine<T>>
    extends StringBuildableExpression<T, Engine>
    implements GenericExpression<T>, GenericTripleExpression<T>
{
    abstract public ParenthesesElisionTrackingInfo getCachedPriorityInfo();
    abstract public DummyParenthesesElisionTrackingInfo getDummyCachedPriorityInfo();
    abstract public void resetCachedPriorityInfo();
}
