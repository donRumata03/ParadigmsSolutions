package expression.general;

import expression.BigDecimalExpression;
import expression.Expression;
import expression.TripleExpression;

/**
 * We want to force implementors to be efficient :)
 */
public abstract class StringBuildableExpression implements Expression, TripleExpression, BigDecimalExpression {
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
