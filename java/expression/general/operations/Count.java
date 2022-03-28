package expression.general.operations;

import expression.general.ParenthesesTrackingExpression;
import expression.general.UnaryOperation;
import expression.general.UnaryOperatorTraits;
import expression.general.arithmetics.ArithmeticEngine;

public class Count<T, Engine extends ArithmeticEngine<T>> extends UnaryOperation<T, Engine> {
    static UnaryOperatorTraits OPERATOR_INFO = new UnaryOperatorTraits(
        "count"
    );

    public Count(ParenthesesTrackingExpression<T> child, Engine engine) {
        super(child, OPERATOR_INFO, engine);
    }

    @Override
    public T reductionOperation(T childResult) {
        return engine.count(childResult);
    }
}