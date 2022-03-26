package search;

import java.util.function.IntPredicate;

@FunctionalInterface
public interface DiscreteBinarySearchEngine {
    /**
     * Let's denote extendedThresholdFunction(x) or just etf(f, l, r) or even `etf` if there's a context
     * for a given thresholdFunction, left and right to be:
     * — false, if x <= left
     * — thresholdFunction(x), if left < x < right
     * — true, if x >= right
     *
     * @param left left border of search
     * @param right > left
     * @param thresholdFunction Is defined in (left, right) and is NSMI at that open interval
     *
     * @return such `DiscreteBinarySearchResult result` (note this type's invariants) that
     * `etf(result.rightmostFalse) == false && etf(result.leftmostTrue) == true`
     */
    DiscreteBinarySearchResult search(IntPredicate thresholdFunction, int left, int right);
}

