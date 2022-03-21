package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;

public interface ArithmeticEngine<Element> {
    Element fromInt(int value);

    Element add(Element left, Element right) throws ExpressionArithmeticException;
    // …
}
