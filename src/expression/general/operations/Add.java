package expression.general.operations;

import expression.general.ArithmeticEngine;
import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;

public class Add<T, Engine extends ArithmeticEngine<T>>
    extends BinaryOperation<T, Engine> {

    public static final OperatorTraits OPERATOR_INFO = new OperatorTraits(
        2,
        true,
        true,
        "+"
    );

    public Add(ParenthesesTrackingExpression<T, Engine> left, ParenthesesTrackingExpression<T, Engine> right, Engine engine) {
        super(left, right, engine, OPERATOR_INFO);
    }

    @Override
    public T reductionOperation(T leftResult, T rightResult) {
        return engine.add(leftResult, rightResult);
    }
}
