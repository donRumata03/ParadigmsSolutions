package expression.generic;

import expression.exceptions.ExpressionParser;
import expression.general.ArithmeticEngine;
import expression.general.BigIntegerArithmetics;
import expression.general.GenericTripleExpression;
import java.math.BigInteger;


public class ExpressionModeSelector {
    GenericExpressionComputer<?, ? extends ArithmeticEngine<?>> computer;

    ExpressionModeSelector(String expression, String mode) {
        computer = switch (mode) {
            case "bi" -> new GenericExpressionComputer<>(expression, new BigIntegerArithmetics());
//            case "" -> {}
            default -> throw new RuntimeException("Unknown command line mode: " + mode);
        };
    }

    String compute(int x, int y, int z) {
        return computer.compute(x, y, z);
    }
}
