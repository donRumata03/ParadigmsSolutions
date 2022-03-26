package expression.general;

/**
 * There are multiple implementations of tracking info, but they share this common interface:
 */
public interface ParenthesesTrackingInfo {

    void includeInParenthesesLessGroup(ParenthesesTrackingInfo other);

    void performParenthesesApplicationDecision(boolean toApplyOrNotToApply);
    boolean parenthesesApplied();
    int getConsideredPriority();
    boolean getConsideredNonAssociativity();
}
