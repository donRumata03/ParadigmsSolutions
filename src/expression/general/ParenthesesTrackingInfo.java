package expression.general;

public interface ParenthesesTrackingInfo {

    void includeInParenthesesLessGroup(ParenthesesTrackingInfo other);

    void performParenthesesApplicationDecision(boolean toApplyOrNotToApply);
    boolean parenthesesApplied();
    int getConsideredPriority();
    boolean getConsideredNonAssociativity();
}
