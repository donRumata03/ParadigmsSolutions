package expression.general;

import expression.general.arithmetics.ArithmeticEngine;
import java.math.BigDecimal;
import java.util.Optional;

public abstract class UnaryOperation<T, Engine extends ArithmeticEngine<T>>
    extends AtomicParenthesesTrackingExpression<T, Engine>
{

    ParenthesesTrackingExpression<T> child;
    UnaryOperatorTraits operatorInfo;

    public UnaryOperation(ParenthesesTrackingExpression<T> child, UnaryOperatorTraits operatorInfo) {
        this.child = child;
        this.operatorInfo = operatorInfo;
    }

    public abstract T reductionOperation(T childResult);

    @Override
    public void toStringBuilder(StringBuilder builder) {
        builder.append(operatorInfo.operatorSymbol()).append("(");
        child.toStringBuilder(builder);
        builder.append(")");
    }

    @Override
    public void toMiniStringBuilderCorrect(StringBuilder builder) {
        var childSPriorityInfo = child.getDummyCachedPriorityInfo();
        childSPriorityInfo.parenthesesApplied =
            childSPriorityInfo.getConsideredPriority() < Integer.MAX_VALUE;

        builder.append(operatorInfo.operatorSymbol());

        if (!childSPriorityInfo.parenthesesApplied) {
            builder.append(" ");
        }

        child.toMiniStringBuilder(builder);
    }

    @Override
    public T evaluate(T x) {
        return reductionOperation(child.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return reductionOperation(child.evaluate(x, y, z));
    }
}
