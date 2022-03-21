package expression.general.operations;

import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import java.math.BigDecimal;


public class LogicalShiftRight extends BinaryOperation {
    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        1,
        false,
        false,
        ">>>"
    );

    public LogicalShiftRight(ParenthesesTrackingExpression left, ParenthesesTrackingExpression right) {
        super(left, right, engine, OPERATOR_INFO);
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
