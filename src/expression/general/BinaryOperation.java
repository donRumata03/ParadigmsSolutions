package expression.general;

import expression.Expression;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class BinaryOperation extends ParenthesesTrackingExpression {
    private final ParenthesesTrackingExpression left;
    private final ParenthesesTrackingExpression right;

    private final OperatorTraits operatorInfo;
    private Optional<ParenthesesTrackingInfo> cachedPriorityInfo = Optional.empty();

    public BinaryOperation(Expression left, Expression right, OperatorTraits operatorInfo) {
        this(new SafestParenthesesTrackingExpressionWrapper(left), new SafestParenthesesTrackingExpressionWrapper(right), operatorInfo);
    }


    public BinaryOperation(ParenthesesTrackingExpression left, ParenthesesTrackingExpression right, OperatorTraits operatorInfo) {
        this.left = left;
        this.right = right;
        this.operatorInfo = operatorInfo;
    }

    public abstract int reductionOperation(int leftResult, int rightResult);
    public abstract BigDecimal reductionOperation(BigDecimal leftResult, BigDecimal rightResult);


    @Override
    public int evaluate(int x) {
        return this.reductionOperation(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return this.reductionOperation(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.reductionOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    /////////////////////////////////////////////////////////



    @Override
    public void resetCachedPriorityInfo() {
        cachedPriorityInfo = Optional.empty();
    }


    public void genericUpdatePriorityInfo(
        ParenthesesTrackingInfo thisInfo, ParenthesesTrackingInfo leftInfo, ParenthesesTrackingInfo rightInfo
    ) {
        // When to ADD brackets:
        if (this.operatorInfo.priority() > leftInfo.getConsideredPriority()) {
            leftInfo.performParenthesesApplicationDecision(true);
        } else {
            thisInfo.includeInParenthesesLessGroup(leftInfo);
        }

        // When to ADD brackets:
        if (this.operatorInfo.priority() > rightInfo.getConsideredPriority()
            || (
                rightInfo.getConsideredPriority() == this.operatorInfo.priority()
                &&
                    (
                        !this.operatorInfo.commutativityAmongPriorityClass()
                            || rightInfo.getConsideredNonAssociativity()
                    )
            )
        ) {
            rightInfo.performParenthesesApplicationDecision(true);
        } else {
            thisInfo.includeInParenthesesLessGroup(rightInfo);
        }
    }


    /**
     *  Make decision if parentheses are necessary for children or not
     *  (It's easy to prove that greedy algorithm makes sense)
     *  — Decisions are made separately for left and right
     *  — If priority of smth is higher => don't have PS
     *  — If priority of smth is lower => have PS
     *  — If priority of smth is the same, for left don't have PS, but for right it becomes more interesting…
     *  — So, for right with same priorities it is removed if: ……………
     */
    public ParenthesesTrackingInfo getGenericCachedPriorityInfo(
        Function<ParenthesesTrackingExpression, ParenthesesTrackingInfo> method,
        Function<OperatorTraits, ParenthesesTrackingInfo> constructor
    ) {
        if (cachedPriorityInfo.isPresent()) {
            return cachedPriorityInfo.get();
        }

        ParenthesesTrackingInfo leftInfo = method.apply(left);
        ParenthesesTrackingInfo rightInfo = method.apply(right);

        cachedPriorityInfo = Optional.of(constructor.apply(operatorInfo));
        ParenthesesTrackingInfo cachingInfo = cachedPriorityInfo.get();

        genericUpdatePriorityInfo(cachingInfo, leftInfo, rightInfo);

        return cachingInfo;
    }

    @Override
    public DummyParenthesesElisionTrackingInfo getDummyCachedPriorityInfo() {
        return (DummyParenthesesElisionTrackingInfo) getGenericCachedPriorityInfo(
            ParenthesesTrackingExpression::getDummyCachedPriorityInfo,
            DummyParenthesesElisionTrackingInfo::new
        );
    }

    @Override
    public ParenthesesElisionTrackingInfo getCachedPriorityInfo() {
        return (ParenthesesElisionTrackingInfo) getGenericCachedPriorityInfo(
            ParenthesesTrackingExpression::getCachedPriorityInfo,
            DummyParenthesesElisionTrackingInfo::new
        );
    }

    /////////////////////////////////////////////////////////

    private void toBaseStringBuilder(StringBuilder builder, boolean addParentheses,
        BiConsumer<ParenthesesTrackingExpression, StringBuilder> buildCaller)
    {
        if (addParentheses) {
            builder.append("(");
        }
        buildCaller.accept(left, builder);

        builder
            .append(" ")
            .append(operatorInfo.operatorSymbol())
            .append(" ");

        buildCaller.accept(right, builder);
        if (addParentheses) {
            builder.append(")");
        }
    }

    @Override
    public void toMiniStringBuilderCorrect(StringBuilder builder) {
        ParenthesesElisionTrackingInfo thisInfo = getCachedPriorityInfo();

        toBaseStringBuilder(builder, thisInfo.parenthesesApplied, ParenthesesTrackingExpression::toMiniStringBuilderCorrect);

        resetCachedPriorityInfo();
    }

    @Override
    public void toMiniStringBuilderDummy(StringBuilder builder) {
        DummyParenthesesElisionTrackingInfo thisInfo = getDummyCachedPriorityInfo();

        toBaseStringBuilder(builder, thisInfo.parenthesesApplied, ParenthesesTrackingExpression::toMiniStringBuilderDummy);

        resetCachedPriorityInfo();
    }


    @Override
    public void toStringBuilder(StringBuilder builder) {
        toBaseStringBuilder(builder, true, ParenthesesTrackingExpression::toStringBuilder);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof BinaryOperation that)) {
            return false;
        }

        return other.getClass() == this.getClass() &&
            operatorInfo.equals(that.operatorInfo) &&
            left.equals(that.left) &&
            right.equals(that.right);
    }

    // TwoArgumentExpression object is immutable
    Optional<Integer> cachedHash = Optional.empty();
    @Override
    public int hashCode() {
        if (cachedHash.isPresent()) {
            return cachedHash.get();
        }
        cachedHash = Optional.of(Objects.hash(left, right, operatorInfo));
        return cachedHash.get();
    }
}
