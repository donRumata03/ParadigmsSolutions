package expression;

import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import expression.general.BinaryOperation;
import java.math.BigDecimal;

public class Add extends BinaryOperation {

    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        2,
        true,
        true,
        "+"
    );

    public Add(ParenthesesTrackingExpression left, ParenthesesTrackingExpression right) {
        super(left, right, engine, OPERATOR_INFO);
    }

    @Override
    public int reductionOperation(int leftResult, int rightResult) {
        return leftResult + rightResult;
    }

    @Override
    public BigDecimal reductionOperation(BigDecimal leftResult, BigDecimal rightResult) {
        return leftResult.add(rightResult);
    }
}
