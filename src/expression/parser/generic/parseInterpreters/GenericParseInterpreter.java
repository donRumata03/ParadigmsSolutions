package expression.parser.generic.parseInterpreters;

import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.ArithmeticEngine;
import expression.parser.generic.tokens.AbstractOperationToken;

public class GenericParseInterpreter<T, Engine extends ArithmeticEngine<T>> implements ParseInterpreter<T> {

    Engine engine;

    GenericParseInterpreter(Engine engine) {
        this.engine = engine;
    }

    @Override
    public T parseSignedInt(String toParse) {
        return engine.parseSignedInt(toParse);
    }

    @Override
    public ParenthesesTrackingExpression<T> constructUnaryExpression(AbstractOperationToken token, ParenthesesTrackingExpression<T> child) {
        return ;
    }

    @Override
    public ParenthesesTrackingExpression<T> constructBinaryExpression(
        AbstractOperationToken token, ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return ;
    }
}
