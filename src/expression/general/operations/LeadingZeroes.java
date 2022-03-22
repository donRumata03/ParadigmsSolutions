package expression.general.operations;

import expression.general.BinaryOperation;
import expression.general.OperatorTraits;
import expression.general.ParenthesesTrackingExpression;
import expression.general.UnaryOperation;
import expression.general.UnaryOperatorTraits;
import expression.general.arithmetics.ArithmeticEngine;
import java.math.BigDecimal;

public class LeadingZeroes<T, Engine extends ArithmeticEngine<T>> extends UnaryOperation<T, Engine> {
    static UnaryOperatorTraits OPERATOR_INFO = new UnaryOperatorTraits(
        "l0"
    );

    public LeadingZeroes(ParenthesesTrackingExpression<T> child, Engine engine) {
        super(child, OPERATOR_INFO, engine);
    }

    @Override
    public T reductionOperation(T childResult) {
        return engine.leadingZeroes(childResult);
    }
}
