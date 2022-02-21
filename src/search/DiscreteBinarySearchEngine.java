package search;

import java.util.function.IntPredicate;

@FunctionalInterface
public interface DiscreteBinarySearchEngine {
    /**
     * @param left left border of search
     * @param right > left
     * @param thresholdFunction Is defined in (left, right) and non-strictly monotonously increase at that open interval
     *
     * Let's denote extendedThresholdFunction(x) for a given thresholdFunction, left and right to be:
     * — false, if x <= left
     * — thresholdFunction(x), if left < x < right
     * — true, if x >= right
     *
     * @return such `DiscreteBinarySearchResult result` (note this type's invariants) that
     * `extendedThresholdFunction(result.rightmostFalse) == false && extendedThresholdFunction(result.leftmostTrue) == true`
     */
    DiscreteBinarySearchResult search(IntPredicate thresholdFunction, int left, int right);
}

