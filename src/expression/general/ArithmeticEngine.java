package expression.general;

import expression.general.exceptions.ExpressionArithmeticException;

public interface ArithmeticEngine<Element> {
    Element add(Element left, Element right) throws ExpressionArithmeticException;
    // …
}
