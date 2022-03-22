package expression.parser.generic.parseInterpreters;

import static expression.parser.generic.tokens.OperatorToken.ARITHMETICAL_SHIFT;
import static expression.parser.generic.tokens.OperatorToken.DIVIDE;
import static expression.parser.generic.tokens.OperatorToken.LOG;
import static expression.parser.generic.tokens.OperatorToken.LOGICAL_SHIFT_RIGHT;
import static expression.parser.generic.tokens.OperatorToken.MINUS;
import static expression.parser.generic.tokens.OperatorToken.MULTIPLY;
import static expression.parser.generic.tokens.OperatorToken.PLUS;
import static expression.parser.generic.tokens.OperatorToken.POW;
import static expression.parser.generic.tokens.OperatorToken.SHIFT_LEFT;

import expression.Add;
import expression.exceptions.CheckedAdd;
import expression.exceptions.CheckedDivide;
import expression.exceptions.CheckedMultiply;
import expression.exceptions.CheckedSubtract;
import expression.general.ParenthesesTrackingExpression;
import expression.parser.generic.tokens.AbstractOperationToken;
import expression.parser.generic.tokens.FunctionToken;
import expression.parser.generic.tokens.OperatorToken;
import expression.parser.generic.tokens.OperatorToken.*;


public abstract class TokenMatcher<Element> implements ParseInterpreter<Element> {

    @Override
    public ParenthesesTrackingExpression<Element> constructUnaryExpression(
        AbstractOperationToken token,
        ParenthesesTrackingExpression<Element> child
    ) {
        assert token.canBeUnary();

        if (token instanceof FunctionToken functionToken) {
            return switch (functionToken) {
                case l0 -> constructLeadingZeroes(child);
                case t0 -> constructTrailingZeroes(child);
                case abs -> constructAbs(child);
            };
        } else if (token instanceof OperatorToken operationToken) {
            return constructUnaryMinus(child);
        }

        throw new UnsupportedOperationException("Unsupported token type");
    }

    @Override
    public ParenthesesTrackingExpression<Element> constructBinaryExpression(
        AbstractOperationToken token,
        ParenthesesTrackingExpression<Element> left,
        ParenthesesTrackingExpression<Element> right
    ) {
        assert token.canBeBinary();

        if (token instanceof FunctionToken functionToken) {
            // TODO: Add min and max
        } else if (token instanceof OperatorToken operationToken) {
            return switch (operationToken) {
                case PLUS -> constructAdd(left, right);
                case MINUS -> constructSubtract(left, right);
                case MULTIPLY -> constructMultiply(left, right);
                case DIVIDE -> constructDivide(left, right);
                case SHIFT_LEFT -> constructShiftLeft(left, right);
                case LOGICAL_SHIFT_RIGHT -> constructLogicalShiftRight(left, right);
                case ARITHMETICAL_SHIFT -> constructArithmeticShiftRight(left, right);
                case POW -> constructPow(left, right);
                case LOG -> constructLog(left, right);
            };
        }

        throw new UnsupportedOperationException("Unsupported token type");
    }

    ///! Required constructing

    // Function tokens; unary
    abstract ParenthesesTrackingExpression<Element> constructLeadingZeroes(ParenthesesTrackingExpression<Element> child);
    abstract ParenthesesTrackingExpression<Element> constructTrailingZeroes(ParenthesesTrackingExpression<Element> child);
    abstract ParenthesesTrackingExpression<Element> constructAbs(ParenthesesTrackingExpression<Element> child);
    // TODO: „count“ function

    // Operator tokens, unary
    abstract ParenthesesTrackingExpression<Element> constructUnaryMinus(ParenthesesTrackingExpression<Element> child);


    // Function tokens, binary
    // TODO: min, max…

    // Operator tokens, binary
    abstract ParenthesesTrackingExpression<Element> constructAdd(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
    abstract ParenthesesTrackingExpression<Element> constructSubtract(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
    abstract ParenthesesTrackingExpression<Element> constructMultiply(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
    abstract ParenthesesTrackingExpression<Element> constructDivide(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
    abstract ParenthesesTrackingExpression<Element> constructShiftLeft(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
    abstract ParenthesesTrackingExpression<Element> constructLogicalShiftRight(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
    abstract ParenthesesTrackingExpression<Element> constructArithmeticShiftRight(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
    abstract ParenthesesTrackingExpression<Element> constructPow(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
    abstract ParenthesesTrackingExpression<Element> constructLog(ParenthesesTrackingExpression<Element> left, ParenthesesTrackingExpression<Element> right);
}
