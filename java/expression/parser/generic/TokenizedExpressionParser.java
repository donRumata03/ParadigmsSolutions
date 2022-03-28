package expression.parser.generic;

import expression.general.arithmetics.ArithmeticEngine;
import expression.general.ParenthesesTrackingExpression;
import expression.parser.generic.parseInterpreters.ParseInterpreter;
import expression.parser.generic.tokens.AbstractOperationToken;
import expression.parser.generic.tokens.FunctionToken;
import expression.parser.generic.tokens.NumberToken;
import expression.parser.generic.tokens.OperatorToken;
import expression.parser.generic.tokens.ParenthesesToken;
import expression.parser.generic.tokens.VariableToken;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public class TokenizedExpressionParser<
    T, Interpreter extends ParseInterpreter<T>
> {
    private final BaseTokenParser tokenParser;
    private final Interpreter interpreter;

    public TokenizedExpressionParser(ArithmeticExpressionTokenizer tokenizer, Interpreter interpreter) {
        this.tokenParser = new BaseTokenParser(tokenizer);
        this.interpreter = interpreter;
    }

    public ParenthesesTrackingExpression<T> parseAll() throws ParseException {
        var result = parseShiftResult();

        if (tokenParser.viewRuntimeErrorizedNextToken().isPresent()) {
            int brokenIndex = tokenParser.getLastTouchedTokenStartIndex();
            throw new ParseException(
                "Can only parse expression[0..%d). There's expression[%d..%d) left starting from token: "
                    .formatted(
                        brokenIndex,
                        brokenIndex,
                        tokenParser.getFirstNonConsumedCharPosition() + tokenParser.consumeCharsLeft()
                    )
                    + tokenParser.viewRuntimeErrorizedNextToken().toString()
            );
        }

        return result;
    }

    private ParenthesesTrackingExpression<T> parseShiftResult() throws ParseException {
        return parseLeftAssociativePriorityLayer(
            TokenizedExpressionParser::parseExpression, List.of(
                OperatorToken.SHIFT_LEFT,
                OperatorToken.ARITHMETICAL_SHIFT,
                OperatorToken.LOGICAL_SHIFT_RIGHT,
                FunctionToken.max,
                FunctionToken.min
            )
        );
    }

    private ParenthesesTrackingExpression<T> parseExpression() throws ParseException {
        return parseLeftAssociativePriorityLayer(TokenizedExpressionParser::parseTerm, List.of(OperatorToken.PLUS, OperatorToken.MINUS));
    }

    private ParenthesesTrackingExpression<T> parseTerm() throws ParseException {
        return parseLeftAssociativePriorityLayer(TokenizedExpressionParser::parseFactor, List.of(OperatorToken.MULTIPLY, OperatorToken.DIVIDE));
    }

    private ParenthesesTrackingExpression<T> parseFactor() throws ParseException {
        return parseLeftAssociativePriorityLayer(TokenizedExpressionParser::parseAtomic, List.of(OperatorToken.POW, OperatorToken.LOG));
    }

    private ParenthesesTrackingExpression<T> parseLeftAssociativePriorityLayer (
        Function<TokenizedExpressionParser<T, Interpreter>, ParenthesesTrackingExpression<T>> prevLayer,
        List<AbstractOperationToken> operators
    ) throws ParseException
    {
        ParenthesesTrackingExpression<T> left = prevLayer.apply(this);

        while (true) {
            var mayBeThisLayerOperation = tokenParser.tryMatchToken(token -> {
                if (!(token instanceof AbstractOperationToken operator)) {
                    return false;
                }
                return operators.contains(operator);
            });
            if (mayBeThisLayerOperation.isEmpty()) {
                break;
            }

            left = interpreter.constructBinaryExpression(
                (AbstractOperationToken)mayBeThisLayerOperation.get(),
                left,
                prevLayer.apply(this)
            );
        }

        return left;
    }

    private String prettyFormatNextToken() {
        return tokenParser.viewRuntimeErrorizedNextToken().isPresent() ?
            tokenParser.viewRuntimeErrorizedNextToken().get().toString()
            : "<EOF>";
    }

    private ParenthesesTrackingExpression<T> parseAtomic() {
        return maybeParsePositiveNumber()
            .or(this::maybeParseVariable)
            .or(this::maybeParseUnaryOperation)
            .or(this::maybeParseExpressionInParentheses)
            .orElseThrow(() -> {
                throw new ParseException(
                    """
                    Atomic expression part is expected at position: %d.
                    It should be one of { number, variable, unaryOp, '(' expr ')' }, but it's %s
                    """.formatted(
                        tokenParser.getLastTouchedTokenStartIndex(),
                        prettyFormatNextToken()
                    )
                );
            });
    }

    private Optional<ParenthesesTrackingExpression<T>> maybeParsePositiveNumber() {
        return tokenParser.tryMatchToken(token -> token instanceof NumberToken)
            .map(token -> interpreter.constructConst(
                interpreter.parseSignedInt(((NumberToken)token).nonParsedValue())
            ));
    }

    private Optional<ParenthesesTrackingExpression<T>> maybeParseUnaryOperation() {
        return tokenParser
            .tryMatchToken(token -> token instanceof AbstractOperationToken operation && operation.canBeUnary())
            .map(unaryOpToken -> {
                if (unaryOpToken == OperatorToken.MINUS) {
                    var tryIntToken = tokenParser.tryMatchToken(t -> t instanceof NumberToken, false);
                    if (tryIntToken.isPresent()) {
                        return interpreter.constructConst(interpreter.parseSignedInt(
                            "-" + ((NumberToken) tryIntToken.get()).nonParsedValue()
                        ));
                    }
                }
                return interpreter.constructUnaryExpression(((AbstractOperationToken)unaryOpToken), parseAtomic());
            });
    }

    private Optional<ParenthesesTrackingExpression<T>> maybeParseVariable() {
        return tokenParser
            .tryMatchToken(token -> token instanceof VariableToken)
            .map(varToken -> interpreter.constructVariable(((VariableToken) varToken).varName()));
    }

    private Optional<ParenthesesTrackingExpression<T>> maybeParseExpressionInParentheses() {
        return tokenParser
            .tryMatchToken(token -> token instanceof ParenthesesToken && ((ParenthesesToken)token).openCloseness())
            .map(p -> {
                var res = parseShiftResult();
                tokenParser.expectNext(tk -> tk instanceof ParenthesesToken && !((ParenthesesToken)tk).openCloseness(),
                """
                „)” is expected after parentheses-surrounded expression part
                (at position %d), but it's %s
                """.formatted(tokenParser.getLastTouchedTokenStartIndex(), prettyFormatNextToken())
                );
                return res;
            });
    }
}
