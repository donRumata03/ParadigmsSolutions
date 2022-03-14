package expression.parser;

import bufferedScanning.ReaderBufferizer;
import expression.TripleExpression;
import expression.parser.Parser;
import expression.parser.generic.ArithmeticExpressionTokenizer;
import expression.parser.generic.ParsableSource;
import expression.parser.generic.TokenizedExpressionParser;
import java.io.StringReader;

public class ExpressionParser implements Parser {

    public ExpressionParser() {}

    @Override
    public TripleExpression parse(String expression) {
        return new TokenizedExpressionParser(
            new ArithmeticExpressionTokenizer(
                new ParsableSource(new ReaderBufferizer(new StringReader(expression)))
            ), false).parseAll();
    }
}
