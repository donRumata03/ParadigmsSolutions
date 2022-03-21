package expression.general;

public interface GenericExpression<T> {
    T evaluate(T x);
}
