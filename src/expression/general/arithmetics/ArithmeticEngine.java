package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;

public interface ArithmeticEngine<Element> {
    Element fromInt(int value);
    Element parseSignedInt(String toParse);

    // Unary stuff
    Element abs(Element argument);
    Element negate(Element argument);
    Element pow(Element left, Element right) throws ExpressionArithmeticException;
    Element log(Element left, Element right) throws ExpressionArithmeticException;


    // Binary stuff
    Element add(Element left, Element right) throws ExpressionArithmeticException;
    Element subtract(Element left, Element right) throws ExpressionArithmeticException;
    Element multiply(Element left, Element right) throws ExpressionArithmeticException;
    Element divide(Element left, Element right) throws ExpressionArithmeticException;

    Element logicalShiftRight(Element left, Element right) throws ExpressionArithmeticException;
    Element logicalShiftLeft(Element left, Element right) throws ExpressionArithmeticException;
    Element arithmeticShiftRight(Element left, Element right) throws ExpressionArithmeticException;

    Element leadingZeroes(Element left, Element right) throws ExpressionArithmeticException;
    Element trailingZeroes(Element left, Element right) throws ExpressionArithmeticException;

    Element max(Element left, Element right) throws ExpressionArithmeticException;
    Element min(Element left, Element right) throws ExpressionArithmeticException;
}
