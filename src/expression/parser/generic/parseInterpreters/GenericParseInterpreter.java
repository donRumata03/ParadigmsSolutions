package expression.parser.generic.parseInterpreters;

import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.ArithmeticEngine;
import expression.general.operations.Abs;
import expression.general.operations.Add;
import expression.general.operations.Subtract;


public class GenericParseInterpreter<T, Engine extends ArithmeticEngine<T>> extends TokenMatcher<T> {

    Engine engine;

    GenericParseInterpreter(Engine engine) {
        this.engine = engine;
    }

    @Override
    public T parseSignedInt(String toParse) {
        return engine.parseSignedInt(toParse);
    }


    @Override
    ParenthesesTrackingExpression<T> constructLeadingZeroes(ParenthesesTrackingExpression<T> child) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructTrailingZeroes(ParenthesesTrackingExpression<T> child) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructAbs(ParenthesesTrackingExpression<T> child) {
        return new Abs<>(child, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructUnaryMinus(ParenthesesTrackingExpression<T> child) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructAdd(ParenthesesTrackingExpression<T> left, ParenthesesTrackingExpression<T> right) {
        return new Add<>(left, right, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructSubtract(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return new Subtract<>(left, right, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructMultiply(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructDivide(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructShiftLeft(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructLogicalShiftRight(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructArithmeticShiftRight(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructPow(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<T> constructLog(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return null;
    }
}
