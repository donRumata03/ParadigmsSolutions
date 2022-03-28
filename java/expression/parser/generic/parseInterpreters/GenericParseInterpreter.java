package expression.parser.generic.parseInterpreters;

import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.ArithmeticEngine;
import expression.general.operations.Abs;
import expression.general.operations.Add;
import expression.general.operations.ArithmeticShiftRight;
import expression.general.operations.Const;
import expression.general.operations.Count;
import expression.general.operations.Divide;
import expression.general.operations.LeadingZeroes;
import expression.general.operations.Log;
import expression.general.operations.LogicalShiftRight;
import expression.general.operations.Max;
import expression.general.operations.Min;
import expression.general.operations.Multiply;
import expression.general.operations.Negate;
import expression.general.operations.Pow;
import expression.general.operations.ShiftLeft;
import expression.general.operations.Subtract;
import expression.general.operations.TrailingZeroes;
import expression.general.operations.Variable;


/**
 * Produces templated operation nodes from expression.general.operations with given engine
 */
public class GenericParseInterpreter<T, Engine extends ArithmeticEngine<T>> extends TokenMatcher<T> {

    Engine engine;

    public GenericParseInterpreter(Engine engine) {
        this.engine = engine;
    }

    @Override
    public T parseSignedInt(String toParse) {
        return engine.parseSignedInt(toParse);
    }

    @Override
    public ParenthesesTrackingExpression<T> constructVariable(String name) {
        return new Variable<T>(name);
    }

    @Override
    public ParenthesesTrackingExpression<T> constructConst(T value) {
        return new Const<>(value);
    }


    @Override
    ParenthesesTrackingExpression<T> constructLeadingZeroes(ParenthesesTrackingExpression<T> child) {
        return new LeadingZeroes<>(child, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructTrailingZeroes(ParenthesesTrackingExpression<T> child) {
        return new TrailingZeroes<>(child, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructAbs(ParenthesesTrackingExpression<T> child) {
        return new Abs<>(child, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructCount(ParenthesesTrackingExpression<T> child) {
        return new Count<>(child, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructUnaryMinus(ParenthesesTrackingExpression<T> child) {
        return new Negate<>(child, engine);
    }

    @Override
    protected ParenthesesTrackingExpression<T> constructMin(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return new Min<>(left, right, engine);
    }

    @Override
    protected ParenthesesTrackingExpression<T> constructMax(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return new Max<>(left, right, engine);
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
        return new Multiply<>(left, right, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructDivide(ParenthesesTrackingExpression<T> left, ParenthesesTrackingExpression<T> right) {
        return new Divide<>(left, right, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructShiftLeft(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return new ShiftLeft<>(left, right, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructLogicalShiftRight(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return new LogicalShiftRight<>(left, right, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructArithmeticShiftRight(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return new ArithmeticShiftRight<>(left, right, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructPow(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return new Pow<>(left, right, engine);
    }

    @Override
    ParenthesesTrackingExpression<T> constructLog(ParenthesesTrackingExpression<T> left,
        ParenthesesTrackingExpression<T> right) {
        return new Log<>(left, right, engine);
    }
}
