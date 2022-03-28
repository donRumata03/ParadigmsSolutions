package expression.parser;

import bufferedScanning.ReaderBufferizer;
import expression.TripleExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import expression.parser.generic.ArithmeticExpressionTokenizer;
import expression.parser.generic.ParsableSource;
import expression.parser.generic.TokenizedExpressionParser;
import expression.parser.generic.parseInterpreters.MaybeCheckedParseInterpreter;
import java.io.StringReader;

public class ExpressionParser implements TripleParser {

    public ExpressionParser() {}

    @Override
    public TripleExpression parse(String expression) {
        return (TripleExpression)
            new TokenizedExpressionParser<Integer, MaybeCheckedParseInterpreter>(
                new ArithmeticExpressionTokenizer(
                    new ParsableSource(new ReaderBufferizer(new StringReader(expression)))
                ),
                new MaybeCheckedParseInterpreter(false)
            ).parseAll();
    }
}
