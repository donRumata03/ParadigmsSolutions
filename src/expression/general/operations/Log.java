package expression.general.operations;

import expression.exceptions.CheckedIntMath;
import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.ArithmeticEngine;
import java.math.BigDecimal;

public class Log<T, Engine extends ArithmeticEngine<T>> extends BinaryOperation<T, Engine> {

    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        4,
        false,
        false,
        "//"
    );

    public Log(ParenthesesTrackingExpression<T> left, ParenthesesTrackingExpression<T> right, Engine engine) {
        super(left, right, engine, OPERATOR_INFO);
    }

    @Override
    public T reductionOperation(T leftResult, T rightResult) {
        return engine.log(leftResult, rightResult);
    }
}
