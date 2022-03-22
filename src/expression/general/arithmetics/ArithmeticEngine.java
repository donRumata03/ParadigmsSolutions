package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;

public interface ArithmeticEngine<Element> {
    Element fromInt(int value);
    Element parseSignedInt(String toParse);


    Element add(Element left, Element right) throws ExpressionArithmeticException;
    // …
}
