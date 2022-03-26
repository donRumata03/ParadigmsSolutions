package expression.general;

import expression.ToMiniString;
import expression.general.arithmetics.ArithmeticEngine;

/**
 * We want to force implementors to be efficient :)
 */
public abstract class StringBuildableExpression<T>
    implements ToMiniString, GenericExpression<T>, GenericTripleExpression<T>
{
    abstract public void toStringBuilder(StringBuilder builder);

    abstract public void toMiniStringBuilderCorrect(StringBuilder builder);
    public void toMiniStringBuilderDummy(StringBuilder builder) {
        toMiniStringBuilderCorrect(builder);
    }
    public void toMiniStringBuilder(StringBuilder builder) {
        toMiniStringBuilderDummy(builder); // Because of the problems in tests
    }

    @Override
    public String toMiniString() {
        StringBuilder builder = new StringBuilder();
        this.toMiniStringBuilder(builder);
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.toStringBuilder(builder);
        return builder.toString();
    }

}
