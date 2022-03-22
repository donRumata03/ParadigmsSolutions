package expression.parser.generic;

import expression.general.arithmetics.ArithmeticEngine;
import expression.general.ParenthesesTrackingExpression;
import expression.parser.generic.tokens.AbstractOperationToken;
import expression.parser.generic.tokens.NumberToken;
import expression.parser.generic.tokens.OperatorToken;
import expression.parser.generic.tokens.ParenthesesToken;
import expression.parser.generic.tokens.VariableToken;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public class TokenizedExpressionParser<T, Engine extends ArithmeticEngine<T>> {
    private final BaseTokenParser tokenParser;
    private boolean checked;

    public TokenizedExpressionParser(ArithmeticExpressionTokenizer tokenizer, boolean checked) {
        this.tokenParser = new BaseTokenParser(tokenizer);
        this.checked = checked;
    }

    public ParenthesesTrackingExpression<T, Engine> parseAll() throws ParseException {
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

    private ParenthesesTrackingExpression parseShiftResult() throws ParseException {
        return parseLeftAssociativePriorityLayer(
            TokenizedExpressionParser::parseExpression, List.of(
                OperatorToken.SHIFT_LEFT,
                OperatorToken.ARITHMETICAL_SHIFT,
                OperatorToken.LOGICAL_SHIFT_RIGHT
            )
        );
    }

    private ParenthesesTrackingExpression parseExpression() throws ParseException {
        return parseLeftAssociativePriorityLayer(TokenizedExpressionParser::parseTerm, List.of(OperatorToken.PLUS, OperatorToken.MINUS));
    }

    private ParenthesesTrackingExpression parseTerm() throws ParseException {
        return parseLeftAssociativePriorityLayer(TokenizedExpressionParser::parseFactor, List.of(OperatorToken.MULTIPLY, OperatorToken.DIVIDE));
    }

    private ParenthesesTrackingExpression parseFactor() throws ParseException {
        return parseLeftAssociativePriorityLayer(TokenizedExpressionParser::parseAtomic, List.of(OperatorToken.POW, OperatorToken.LOG));
    }

    private ParenthesesTrackingExpression parseLeftAssociativePriorityLayer (
        Function<TokenizedExpressionParser, ParenthesesTrackingExpression> prevLayer,
        List<OperatorToken> operators
    ) throws ParseException
    {
        ParenthesesTrackingExpression left = prevLayer.apply(this);

        while (true) {
            var mayBeOperator = tokenParser.tryMatchToken(token -> {
                if (!(token instanceof OperatorToken operator)) {
                    return false;
                }
                return operators.contains(operator);
            });
            if (mayBeOperator.isEmpty()) {
                break;
            }

            left = ((AbstractOperationToken)mayBeOperator.get()).constructBinaryExpression(left, prevLayer.apply(this), checked);
        }

        return left;
    }

    private String prettyFormatNextToken() {
        return tokenParser.viewRuntimeErrorizedNextToken().isPresent() ?
            tokenParser.viewRuntimeErrorizedNextToken().get().toString()
            : "<EOF>";
    }

    private ParenthesesTrackingExpression parseAtomic() {
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

    private Optional<ParenthesesTrackingExpression> maybeParsePositiveNumber() {
        return tokenParser.tryMatchToken(token -> token instanceof NumberToken)
            .map(token -> new Const(
                Integer.parseInt(((NumberToken)token).nonParsedValue())
            ));
    }

    private Optional<ParenthesesTrackingExpression> maybeParseUnaryOperation() {
        return tokenParser
            .tryMatchToken(token -> token instanceof AbstractOperationToken operation && operation.canBeUnary())
            .map(unaryOpToken -> {
                if (unaryOpToken == OperatorToken.MINUS) {
                    var tryIntToken = tokenParser.tryMatchToken(t -> t instanceof NumberToken, false);
                    if (tryIntToken.isPresent()) {
                        return new Const(Integer.parseInt(
                            "-" + ((NumberToken)tryIntToken.get()).nonParsedValue()
                        ));
                    }
                }
                return ((AbstractOperationToken)unaryOpToken).constructUnaryExpression(parseAtomic(), checked);
            });
    }

    private Optional<ParenthesesTrackingExpression> maybeParseVariable() {
        return tokenParser
            .tryMatchToken(token -> token instanceof VariableToken)
            .map(varToken -> new Variable(((VariableToken)varToken).varName()));
    }

    private Optional<ParenthesesTrackingExpression> maybeParseExpressionInParentheses() {
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
