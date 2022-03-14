package expression.general;

public class DummyParenthesesElisionTrackingInfo implements ParenthesesTrackingInfo {
    public boolean parenthesesApplied;
    public int lowestPriorityAfterParentheses;
    public boolean isAssociativeAmongPriorityClass;


    public DummyParenthesesElisionTrackingInfo(boolean parenthesesApplied, int priority, boolean isAssociativeAmongPriorityClass) {
        this.parenthesesApplied = parenthesesApplied;
        this.lowestPriorityAfterParentheses = priority;
        this.isAssociativeAmongPriorityClass = isAssociativeAmongPriorityClass;
    }

    public DummyParenthesesElisionTrackingInfo(OperatorTraits fromOperator) {
        this(false, fromOperator.priority(), fromOperator.associativityAmongPriorityClass());
    }

    @Override
    public void includeInParenthesesLessGroup(ParenthesesTrackingInfo o) {
        if (!(o instanceof DummyParenthesesElisionTrackingInfo other)) {
            return;
        }

        this.lowestPriorityAfterParentheses = Integer.min(
            this.lowestPriorityAfterParentheses,
            other.lowestPriorityAfterParentheses
        );
    }

    @Override
    public void performParenthesesApplicationDecision(boolean toApplyOrNotToApply) {
        parenthesesApplied = toApplyOrNotToApply;
    }

    @Override
    public boolean parenthesesApplied() {
        return parenthesesApplied;
    }

    @Override
    public int getConsideredPriority() {
        return lowestPriorityAfterParentheses;
    }

    @Override
    public boolean getConsideredNonAssociativity() {
        return !isAssociativeAmongPriorityClass;
    }


    static DummyParenthesesElisionTrackingInfo neutralElement() {
        return generateAtomicExpressionInfo();
    }

    public static DummyParenthesesElisionTrackingInfo generateAtomicExpressionInfo() {
        return new DummyParenthesesElisionTrackingInfo(
            false,
            Integer.MAX_VALUE,
            true
        );
    }

    public static DummyParenthesesElisionTrackingInfo generateSafestExpressionInfo() {
        return new DummyParenthesesElisionTrackingInfo(
            false,
            Integer.MIN_VALUE,
            false
        );
    }
}
