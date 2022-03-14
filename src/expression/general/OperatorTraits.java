package expression.general;

import java.util.Objects;

public record OperatorTraits(
    int priority,
    boolean commutativityAmongPriorityClass,
    boolean associativityAmongPriorityClass,
    String operatorSymbol
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperatorTraits that)) {
            return false;
        }
        return priority == that.priority &&
            commutativityAmongPriorityClass == that.commutativityAmongPriorityClass &&
            associativityAmongPriorityClass == that.associativityAmongPriorityClass &&
            operatorSymbol.equals(that.operatorSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            priority,
            commutativityAmongPriorityClass,
            associativityAmongPriorityClass,
            operatorSymbol
        );
    }
}
