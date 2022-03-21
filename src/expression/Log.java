package expression;

import expression.exceptions.CheckedIntMath;
import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import java.math.BigDecimal;

public class Log extends BinaryOperation {

    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        4,
        false,
        false,
        "//"
    );

    public Log(ParenthesesTrackingExpression left, ParenthesesTrackingExpression right) {
        super(left, right, engine, OPERATOR_INFO);
    }

    @Override
    public int reductionOperation(int leftResult, int rightResult) {
        return CheckedIntMath.checkedLog(leftResult, rightResult);
    }

    @Override
    public BigDecimal reductionOperation(BigDecimal leftResult, BigDecimal rightResult) {
        return null;
    }
}
