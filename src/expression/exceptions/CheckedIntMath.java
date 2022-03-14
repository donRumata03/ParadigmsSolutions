package expression.exceptions;


import expression.general.exceptions.IntegerArithmeticException;
import expression.general.exceptions.IntegerOverflowException;

public class CheckedIntMath {

    public static boolean areOpposite(int left, int right) {
        return Integer.min(left, right) == -Integer.max(left, right);
    }

    public static int sgn(int value) {
        return Integer.signum(value);
    }


    public static int checkedNegate(int input) {
        if (input == Integer.MIN_VALUE) {
            throw new IntegerOverflowException("Can't negate minimal value in two's complement");
        }
        return -input;
    }

    public static int checkedAbs(int input) {
        if (input == Integer.MIN_VALUE) {
            throw new IntegerArithmeticException("Can't compute absolute value for Integer.MIN_VALUE");
        }

        return input < 0 ? -input : input;
    }

    public static int checkedAdd(int left, int right) throws IntegerOverflowException {
        int uncheckedResult = left + right;

        if (sgn(left) == sgn(right)) {
            int bothResultsSign = sgn(left);
            if (bothResultsSign != sgn(uncheckedResult)) {
                throw new IntegerOverflowException(
                    (bothResultsSign == 1 ? "Overflow" : "Underflow")
                        + " has occurred while adding integers: " + left + " and " + right);
            }
        }

        return uncheckedResult;
    }

    public static int checkedSubtract(int left, int right) throws IntegerOverflowException {
        int uncheckedResult = left - right;

        int sgnLeft = sgn(left);
        int sgnRight = sgn(right);
        int sgnSub = sgn(uncheckedResult);
        if (sgnLeft != sgnRight && right != 0) {
            int signMustBe = -sgnRight;
            if (signMustBe != sgnSub) {
                throw new IntegerOverflowException(
                    (sgnSub == 1 ? "Overflow" : "Underflow")
                        + " has occurred while adding integers: " + left + " and " + right);
            }
        }

        return uncheckedResult;
    }

    public static int checkedMultiply(int left, int right) throws IntegerOverflowException {
        int proposedResult = left * right;

        if ((left != 0 && proposedResult / left != right)
            || (right == -1 && left == Integer.MIN_VALUE)
            || (right == Integer.MIN_VALUE && left == -1)
        ) {
            throw new IntegerOverflowException(
                "Overflow occurred while multiplying integers: " + right + " and " + left
            );
        }

        return proposedResult;
    }

    public static int checkedDivide(int left, int right) throws IntegerOverflowException, IntegerArithmeticException {
        if (right == 0) {
            throw new IntegerArithmeticException("Can't divide by zero");
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new IntegerOverflowException("Overflow occurred when dividing Integer.MIN_VALUE by -1");
        }

        return left / right;
    }

    public static int checkedPow(int left, int right) {
        if (right < 0 || left == 0 && right == 0) {
            throw new IntegerArithmeticException("Invalid pow arguments");
        }

        if (right == 0) {
            return 1;
        }
        if (right == 1) {
            return left;
        }

        if (right % 2 == 0) {
            int root = checkedPow(left, right / 2);
            return checkedMultiply(root, root);
        } else {
            return checkedMultiply(left, checkedPow(left, right - 1));
        }
    }

    public static int checkedLog(int left, int right) {
        if (left <= 0 || right <= 1) {
            throw new IntegerArithmeticException("Invalid log arguments");
        }

        int divisions = 0;
        while (left >= right) {
            left /= right;
            divisions++;
        }

        return divisions;
    }
}
