package search;

import java.util.function.Function;

public class DiscreteRecursiveBinarySearch {

    /**
     * Satisfies «Pre- and post- conditions for binary search» ↑↑↑
     */
    static DiscreteBinarySearchResult discreteRecursiveBinarySearch(Function<Integer, Boolean> thresholdFunction, int left, int right) {
        // extendedThresholdFunction non-strictly monotonously increase at [left, right] &&
        // extendedThresholdFunction(left) == false && extendedThresholdFunction(right) == true &&
        // left < right

        if (left + 1 == right) {
            // left + 1 == right (=> can construct DiscreteBinarySearchResult)
            // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true
            // (=> `extendedThresholdFunction(result.rightmostFalse) == false && extendedThresholdFunction(result.leftmostTrue) == true`)
            return new DiscreteBinarySearchResult(left, right); // so, function's post-condition is satisfied
        }

        // extendedThresholdFunction non-strictly monotonously increase at [left, right] &&
        // extendedThresholdFunction(left) == false && extendedThresholdFunction(right) == true &&
        // left + 1 < right &&
        // (=>) (left + right) / 2 - left >= 1 &&
        // (=>) right - (left + right) / 2 >= 1
        int m = (left + right) / 2;
        // extendedThresholdFunction non-strictly monotonously increase at [left, right] &&
        // extendedThresholdFunction(left) == false && extendedThresholdFunction(right) == true &&
        // left + 1 < right &&
        // m - left >= 1 &&
        // right - m >= 1 &&
        // (=>) m \in (left, right)

        // (m - left >= 1 && right - m >= 1) => pre-condition for `thresholdFunction` is satisfied
        boolean callResult = thresholdFunction.apply(m);
        if (callResult) {
            // extendedThresholdFunction non-strictly monotonously increase at [left, right] &&
            // extendedThresholdFunction(left) == false && extendedThresholdFunction(right) == true &&
            // left + 1 < right &&
            // m \in (left, right)
            // && extendedThresholdFunction(m) == true

            // (=>) extendedThresholdFunction non-strictly monotonously increase at [left, m] &&
            // (=>) extendedThresholdFunction(left) == false && extendedThresholdFunction(m) == true
            // (=>) m > left

            // m - left + 1 <= right - left => recursion isn't infinite
            return discreteRecursiveBinarySearch(thresholdFunction, left, m);
        } else {
            // extendedThresholdFunction non-strictly monotonously increase at [left, right] &&
            // extendedThresholdFunction(left) == false && extendedThresholdFunction(right) == true &&
            // left + 1 < right &&
            // m \in (left, right)
            // && extendedThresholdFunction(m) == false

            // (=>) extendedThresholdFunction non-strictly monotonously increase at [m, right] &&
            // (=>) extendedThresholdFunction(m) == false && extendedThresholdFunction(right) == true
            // (=>) right > m

            // right - m + 1 <= right - left => recursion isn't infinite
            return discreteRecursiveBinarySearch(thresholdFunction, m, right);
        }
    }

}
