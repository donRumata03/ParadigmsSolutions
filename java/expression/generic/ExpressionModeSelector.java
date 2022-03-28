package expression.generic;

import expression.general.arithmetics.ArithmeticEngine;
import expression.general.arithmetics.BigIntegerArithmetics;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.arithmetics.UncheckedDoubleArithmetics;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import expression.general.arithmetics.UncheckedLongArithmetics;


/**
 * Depending on mode string builds proper expression and wraps it providing unified int → String interface
 */
public class ExpressionModeSelector {
    GenericExpressionComputer<?, ? extends ArithmeticEngine<?>> computer;

    ExpressionModeSelector(String expression, String mode) {
        computer = switch (mode) {
            case "i" -> new GenericExpressionComputer<>(expression, new CheckedIntegerArithmetics());
            case "d" -> new GenericExpressionComputer<>(expression, new UncheckedDoubleArithmetics());
            case "bi" -> new GenericExpressionComputer<>(expression, new BigIntegerArithmetics());
            case "u" -> new GenericExpressionComputer<>(expression, new UncheckedIntegerArithmetics());
            case "l" -> new GenericExpressionComputer<>(expression, new UncheckedLongArithmetics());
            case "t" -> new GenericExpressionComputer<>(expression, new TrucatedIntegerArithmetics());
//            case "" -> {}
            default -> throw new RuntimeException("Unknown command line mode: " + mode);
        };
    }

    Object compute(int x, int y, int z) {
        return computer.compute(x, y, z);
    }
}
