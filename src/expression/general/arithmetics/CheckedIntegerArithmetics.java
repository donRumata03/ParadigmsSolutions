package expression.general.arithmetics;

import expression.exceptions.CheckedIntMath;
import expression.general.exceptions.ExpressionArithmeticException;
import expression.general.exceptions.IntegerArithmeticException;

public class CheckedIntegerArithmetics implements ArithmeticEngine<Integer> {

        @Override
        public Integer fromInt(int value) {
            return value;
        }

        @Override
        public Integer add(Integer left, Integer right) throws ExpressionArithmeticException {
            return CheckedIntMath.checkedAdd(left, right);
        }

        // …
    }
