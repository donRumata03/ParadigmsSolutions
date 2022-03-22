package expression.general.operations;

import expression.exceptions.CheckedIntMath;
import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import expression.general.UnaryOperation;
import expression.general.UnaryOperatorTraits;
import expression.general.arithmetics.ArithmeticEngine;
import java.math.BigDecimal;

public class Abs<T, Engine extends ArithmeticEngine<T>> extends UnaryOperation<T, Engine> {

    public static final UnaryOperatorTraits OPERATOR_INFO = new UnaryOperatorTraits("abs");

    public Abs(ParenthesesTrackingExpression<T> child, Engine engine) {
        super(child, OPERATOR_INFO, engine);
    }

    @Override
    public T reductionOperation(T childResult) {
        return engine.abs(childResult);
    }
}
