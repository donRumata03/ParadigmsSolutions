package expression.general.operations;

import expression.general.ParenthesesTrackingExpression;
import expression.general.UnaryOperation;
import expression.general.UnaryOperatorTraits;
import java.math.BigDecimal;


public class TrailingZeroes extends UnaryOperation {
    static UnaryOperatorTraits OPERATOR_TRAITS = new UnaryOperatorTraits(
        "t0"
    );

    public TrailingZeroes(ParenthesesTrackingExpression child) {
        super(child, OPERATOR_TRAITS);
    }

    @Override
    public int reductionOperation(int childResult) {
        return Integer.numberOfTrailingZeros(childResult);
    }

    @Override
    public BigDecimal reductionOperation(BigDecimal childResult) {
        throw new RuntimeException("Works only with integers");
    }
}

