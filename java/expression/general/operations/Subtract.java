package expression.general.operations;

import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.ArithmeticEngine;

public class Subtract<T, Engine extends ArithmeticEngine<T>> extends BinaryOperation<T, Engine> {

    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        2,
        false,
        true,
        "-"
    );

    public Subtract(ParenthesesTrackingExpression<T> left, ParenthesesTrackingExpression<T> right, Engine engine) {
        super(left, right, engine, OPERATOR_INFO);
    }


    @Override
    public T reductionOperation(T leftResult, T rightResult) {
        return engine.subtract(leftResult, rightResult);
    }
}
