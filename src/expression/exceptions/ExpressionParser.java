package expression.exceptions;

import bufferedScanning.ReaderBufferizer;
import expression.TripleExpression;
import expression.parser.generic.ParseException;
import expression.parser.generic.ArithmeticExpressionTokenizer;
import expression.parser.generic.ParsableSource;
import expression.parser.generic.TokenizedExpressionParser;
import java.io.StringReader;

public class ExpressionParser implements Parser {

    @Override
    public TripleExpression parse(String expression) throws ParseException {
        return new TokenizedExpressionParser(
            new ArithmeticExpressionTokenizer(
                new ParsableSource(new ReaderBufferizer(new StringReader(expression)))
            ), true).parseAll();
    }
}
