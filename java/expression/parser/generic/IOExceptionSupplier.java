package expression.parser.generic;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionSupplier<T> {
    T get() throws IOException;
}
