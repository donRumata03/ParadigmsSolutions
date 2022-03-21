package expression.general.operations;

import expression.exceptions.CheckedIntMath;
import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import java.math.BigDecimal;

public class Pow extends BinaryOperation {

    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        4,
        false,
        false,
        "**"
    );

    public Pow(ParenthesesTrackingExpression left, ParenthesesTrackingExpression right) {
        super(left, right, engine, OPERATOR_INFO);
    }

    @Override
    public int reductionOperation(int leftResult, int rightResult) {
        return CheckedIntMath.checkedPow(leftResult, rightResult);
    }

    @Override
    public BigDecimal reductionOperation(BigDecimal leftResult, BigDecimal rightResult) {
        return null;
    }
}
