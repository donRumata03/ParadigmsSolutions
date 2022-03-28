package expression.general.exceptions;


public class ExpressionArithmeticException extends RuntimeException {
    public ExpressionArithmeticException(String message) {
        super(message);
    }
    public ExpressionArithmeticException(ArithmeticException cause) {
        super(cause);
    }

    @FunctionalInterface
    public interface ArithmeticSupplier<T> {
        T supply() throws ArithmeticException;
    }

    public static <T> T wrapComputationWithException(ArithmeticSupplier<T> computer) throws ExpressionArithmeticException {
        try {
            return computer.supply();
        } catch (ArithmeticException e) {
            throw new ExpressionArithmeticException(e);
        }
    }
}
