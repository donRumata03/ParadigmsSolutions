package expression.parser.generic;

import expression.parser.generic.tokens.ArithmeticExpressionToken;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

public class BaseTokenParser {

    private final ArithmeticExpressionTokenizer tokenizer;

    public BaseTokenParser(ArithmeticExpressionTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Optional<ArithmeticExpressionToken> tryMatchToken(Predicate<ArithmeticExpressionToken> predicate, boolean allowSpaceSkipping) {
        if (
            viewRuntimeErrorizedNextToken(allowSpaceSkipping).isPresent() &&
                predicate.test(viewRuntimeErrorizedNextToken(allowSpaceSkipping).get())
        ) {
            return consumeRuntimeErrorizedNextToken();
        }

        return Optional.empty();
    }

    public Optional<ArithmeticExpressionToken> tryMatchToken(Predicate<ArithmeticExpressionToken> predicate) {
        return tryMatchToken(predicate, true);
    }

    public int getLastTouchedTokenStartIndex() {
        return tokenizer.getLastTouchedTokenStartIndex();
    }

    public int consumeCharsLeft() {
        return runtimeErrorizeIOException(tokenizer::consumeCharsLeft);
    }

    public int getFirstNonConsumedCharPosition() {
        return runtimeErrorizeIOException(tokenizer::getFirstNonConsumedCharPosition);
    }

//    public Optional<List<ArithmeticExpressionToken>> tryMatchTokenSequence(List<Predicate<ArithmeticExpressionToken>> checkers) {
//        List<ArithmeticExpressionToken> res = new ArrayList<>();
//
//        for (var checker : checkers) {
//            if ()
//        }
//
//        if (viewRuntimeErrorizedNextToken().isPresent() && predicate.test(viewRuntimeErrorizedNextToken().get())) {
//            return consumeRuntimeErrorizedNextToken();
//        }
//
//        return Optional.empty();
//    }

    public void expectNext(Predicate<ArithmeticExpressionToken> predicate) throws ParseException {
        expectNext(predicate, "");
    }

    public void expectNext(Predicate<ArithmeticExpressionToken> predicate, String message) throws ParseException {
        if (viewRuntimeErrorizedNextToken().isEmpty() || !predicate.test(viewRuntimeErrorizedNextToken().get())) {
            throw new ParseException(message);
        }
        consumeRuntimeErrorizedNextToken();
    }


    public Optional<ArithmeticExpressionToken> consumeRuntimeErrorizedNextToken(boolean allowSpaceSkipping) {
        return runtimeErrorizeIOException(() -> tokenizer.nextToken(allowSpaceSkipping));
    }

    public Optional<ArithmeticExpressionToken> viewRuntimeErrorizedNextToken(boolean allowSpaceSkipping) {
        return runtimeErrorizeIOException(() -> tokenizer.viewNextToken(allowSpaceSkipping));
    }

    public Optional<ArithmeticExpressionToken> consumeRuntimeErrorizedNextToken() {
        return runtimeErrorizeIOException(tokenizer::nextToken);
    }

    public Optional<ArithmeticExpressionToken> viewRuntimeErrorizedNextToken() {
        return runtimeErrorizeIOException(tokenizer::viewNextToken);
    }

    public static <T> T runtimeErrorizeIOException(IOExceptionSupplier<T> ioSupplier) {
        try {
            return ioSupplier.get();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred while parsing", e);
        }
    }
}
