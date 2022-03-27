package expression.generic;

import expression.general.arithmetics.ArithmeticEngine;
import expression.general.GenericTripleExpression;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import expression.parser.ExpressionParser;
import expression.parser.generic.parseInterpreters.ParseInterpreter;



public class GenericExpressionComputer<T, Engine extends ArithmeticEngine<T>> {

    Engine engine;
    GenericTripleExpression<T> expression;

    GenericExpressionComputer(String expression, Engine engine) {
        this.engine = engine;
        this.expression = new GenericParser<>(engine).parse(expression);
    }

    Object compute(int x, int y, int z) {
        return expression.evaluate(engine.fromInt(x), engine.fromInt(y), engine.fromInt(z));
    }

}
