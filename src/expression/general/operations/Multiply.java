package expression.general.operations;

import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import java.math.BigDecimal;

public class Multiply extends BinaryOperation {

    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        3,
        true,
        true,
        "*"
    );

    public Multiply(ParenthesesTrackingExpression left, ParenthesesTrackingExpression right) {
        super(left, right, OPERATOR_INFO);
    }

    @Override
    public int reductionOperation(int leftResult, int rightResult) {
        return leftResult * rightResult;
    }

    @Override
    public BigDecimal reductionOperation(BigDecimal leftResult, BigDecimal rightResult) {
        return leftResult.multiply(rightResult);
    }
}
