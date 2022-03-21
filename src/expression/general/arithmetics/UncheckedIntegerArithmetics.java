package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;
public class UncheckedIntegerArithmetics implements ArithmeticEngine<Integer> {

        @Override
        public Integer fromInt(int value) {
            return value;
        }

        @Override
        public Integer add(Integer left, Integer right) {
            return left + right;
        }

        // …
    }
