package expression.exceptions;

import expression.TripleExpression;
import expression.parser.generic.ParseException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FunctionalInterface
public interface Parser {
    TripleExpression parse(String expression) throws ParseException;
}
