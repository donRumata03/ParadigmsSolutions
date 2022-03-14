package expression;

import expression.general.ParenthesesTrackingExpression;
import expression.general.UnaryOperation;
import expression.general.UnaryOperatorTraits;
import java.math.BigDecimal;

public class LeadingZeroes extends UnaryOperation {
    static UnaryOperatorTraits OPERATOR_TRAITS = new UnaryOperatorTraits(
        "l0"
    );

    public LeadingZeroes(ParenthesesTrackingExpression child) {
        super(child, OPERATOR_TRAITS);
    }

    @Override
    public int reductionOperation(int childResult) {
        return Integer.numberOfLeadingZeros(childResult);
    }

    @Override
    public BigDecimal reductionOperation(BigDecimal childResult) {
        throw new RuntimeException("Works only with integers");
    }
}
