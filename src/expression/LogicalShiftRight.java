package expression;

import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import expression.general.BinaryOperation;
import java.math.BigDecimal;


public class LogicalShiftRight extends BinaryOperation {
    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        1,
        false,
        false,
        ">>>"
    );

    public LogicalShiftRight(ParenthesesTrackingExpression left, ParenthesesTrackingExpression right) {
        super(left, right, OPERATOR_INFO);
    }

    @Override
    public int reductionOperation(int leftResult, int rightResult) {
        return leftResult >>> rightResult;
    }

    @Override
    public BigDecimal reductionOperation(BigDecimal leftResult, BigDecimal rightResult) {
        throw new RuntimeException("Only int supported");
    }
}
