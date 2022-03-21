package expression.general;

public interface GenericTripleExpression<T> {
    T evaluate(T x, T y, T z);
}
