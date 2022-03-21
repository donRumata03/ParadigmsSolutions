package expression.generic;

import expression.general.ArithmeticEngine;
import expression.general.GenericTripleExpression;

public class GenericExpressionComputer<T, Engine extends ArithmeticEngine<T>> {

    Engine engine;
    GenericTripleExpression<T> expression;

    GenericExpressionComputer(String expression, Engine engine) {
        this.engine = engine;
        this.expression = new expression.exceptions.ExpressionParser().parse(expression);
    }

    String compute(int x, int y, int z) {
        return expression.evaluate(engine.fromInt(x), engine.fromInt(y), engine.fromInt(z)).toString();
    }

}
