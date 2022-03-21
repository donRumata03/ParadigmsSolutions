package expression.generic;

import bufferedScanning.ReaderBufferizer;
import expression.TripleExpression;
import expression.general.ArithmeticEngine;
import expression.general.GenericTripleExpression;
import expression.parser.generic.ArithmeticExpressionTokenizer;
import expression.parser.generic.ParsableSource;
import expression.parser.generic.ParseException;
import expression.parser.generic.TokenizedExpressionParser;
import java.io.StringReader;

public class GenericParser<T, Engine extends ArithmeticEngine<T>> {
    public GenericTripleExpression<T> parse(String expression) throws ParseException {
        return new TokenizedExpressionParser<T, Engine>(
            new ArithmeticExpressionTokenizer(
                new ParsableSource(new ReaderBufferizer(new StringReader(expression)))
            ), true).parseAll();
    }
}
