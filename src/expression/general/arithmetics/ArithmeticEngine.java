package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;

public interface ArithmeticEngine<Element> {
    Element fromInt(int value);
    Element parseSignedInt(String toParse);

    // Unary stuff
    Element abs(Element argument);

    // Binary stuff
    Element add(Element left, Element right) throws ExpressionArithmeticException;

}
