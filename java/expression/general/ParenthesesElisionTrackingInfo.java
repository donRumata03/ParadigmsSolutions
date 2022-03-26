package expression.general;

public class ParenthesesElisionTrackingInfo implements ParenthesesTrackingInfo {
    public boolean parenthesesApplied;

    public int lowestPriorityAfterParentheses;
    public boolean containsNonAssociativeLowestPriorityAfterParentheses;

    public ParenthesesElisionTrackingInfo(boolean parenthesesApplied, int lowestPriorityAfterParentheses,
        boolean containsNonAssociativeLowestPriorityAfterParentheses)
    {
        this.parenthesesApplied = parenthesesApplied;
        this.lowestPriorityAfterParentheses = lowestPriorityAfterParentheses;
        this.containsNonAssociativeLowestPriorityAfterParentheses = containsNonAssociativeLowestPriorityAfterParentheses;
    }

    public ParenthesesElisionTrackingInfo(OperatorTraits fromOperator) {
        this(
            false,
            fromOperator.priority(),
            !fromOperator.associativityAmongPriorityClass()
        );
    }

    @Override
    public void includeInParenthesesLessGroup(ParenthesesTrackingInfo o) {
        if (!(o instanceof ParenthesesElisionTrackingInfo other)) {
            return;
        }

        // this.lowestPriorityAfterParentheses <= priority of current operation

        // Should update
        // — «lowestPriorityAfterParentheses»
        // — «containsNonAssociativeLowestPriorityAfterParentheses»
        if (this.lowestPriorityAfterParentheses > other.lowestPriorityAfterParentheses) {
            // Accept theirs:
            this.lowestPriorityAfterParentheses = other.lowestPriorityAfterParentheses;
            this.containsNonAssociativeLowestPriorityAfterParentheses =
                other.containsNonAssociativeLowestPriorityAfterParentheses;

        } else if (this.lowestPriorityAfterParentheses == other.lowestPriorityAfterParentheses) {
            // Combine having associative:
            this.containsNonAssociativeLowestPriorityAfterParentheses |=
                other.containsNonAssociativeLowestPriorityAfterParentheses;

        } else /* thisPriority < other.lowestPriorityAfterParentheses */ {
            // pass (accept «this» data)
        }
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
        return containsNonAssociativeLowestPriorityAfterParentheses;
    }


    public static ParenthesesElisionTrackingInfo generateAtomicExpressionInfo() {
        return new ParenthesesElisionTrackingInfo(
            false,
            Integer.MAX_VALUE,
            false
        );
    }

    public static ParenthesesElisionTrackingInfo generateSafestExpressionInfo() {
        return new ParenthesesElisionTrackingInfo(
            false,
            Integer.MIN_VALUE,
            true
        );
    }

    public static ParenthesesElisionTrackingInfo neutralElement() {
        return new ParenthesesElisionTrackingInfo(
            false,
            Integer.MAX_VALUE,
            false
        );
    }
}
