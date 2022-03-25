package expression.generic;

import bufferedScanning.ReaderBufferizer;
import expression.general.arithmetics.ArithmeticEngine;
import expression.general.GenericTripleExpression;
import expression.parser.generic.ArithmeticExpressionTokenizer;
import expression.parser.generic.ParsableSource;
import expression.parser.generic.ParseException;
import expression.parser.generic.TokenizedExpressionParser;
import expression.parser.generic.parseInterpreters.GenericParseInterpreter;
import java.io.StringReader;

public record GenericParser<T, Engine extends ArithmeticEngine<T>>(Engine engine) {

    public GenericTripleExpression<T> parse(String expression) throws ParseException {
        return new TokenizedExpressionParser<>(
            new ArithmeticExpressionTokenizer(
                new ParsableSource(new ReaderBufferizer(new StringReader(expression)))
            ),
            new GenericParseInterpreter<>(engine)
        ).parseAll();
    }
}
