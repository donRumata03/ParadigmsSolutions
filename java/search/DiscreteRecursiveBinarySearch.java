package search;

import java.util.function.IntPredicate;

public class DiscreteRecursiveBinarySearch {

    /**
     * Satisfies «Pre- and post- conditions for binary search» ↑↑↑
     */
    static DiscreteBinarySearchResult discreteRecursiveBinarySearch(IntPredicate thresholdFunction, int left, int right) {
        // etf non-strictly monotonously increase at [left, right] &&
        // etf(left) == false && etf(right) == true &&
        // left < right

        if (left + 1 == right) {
            // left + 1 == right (=> can construct DiscreteBinarySearchResult)
            // etf(l) == false && etf(r) == true
            // (=> `etf(result.rightmostFalse) == false && etf(result.leftmostTrue) == true`)
            return new DiscreteBinarySearchResult(left, right); // so, function's post-condition is satisfied
        }

        // etf non-strictly monotonously increase at [left, right] &&
        // etf(left) == false && etf(right) == true &&
        // left + 1 < right &&
        // (=>) (left + right) / 2 - left >= 1 &&
        // (=>) right - (left + right) / 2 >= 1
        int m = (left + right) / 2;
        // etf non-strictly monotonously increase at [left, right] &&
        // etf(left) == false && etf(right) == true &&
        // left + 1 < right &&
        // m - left >= 1 &&
        // right - m >= 1 &&
        // (=>) m \in (left, right)

        // (m - left >= 1 && right - m >= 1) => pre-condition for `thresholdFunction` is satisfied
        boolean callResult = thresholdFunction.test(m);
        if (callResult) {
            // etf non-strictly monotonously increase at [left, right] &&
            // etf(left) == false && etf(right) == true &&
            // left + 1 < right &&
            // m \in (left, right)
            // && etf(m) == true

            // (=>) etf non-strictly monotonously increase at [left, m] &&
            // (=>) etf(left) == false && etf(m) == true
            // (=>) m > left

            // m - left + 1 <= right - left => recursion isn't infinite
            return discreteRecursiveBinarySearch(thresholdFunction, left, m);
        } else {
            // etf non-strictly monotonously increase at [left, right] &&
            // etf(left) == false && etf(right) == true &&
            // left + 1 < right &&
            // m \in (left, right)
            // && etf(m) == false

            // (=>) etf non-strictly monotonously increase at [m, right] &&
            // (=>) etf(m) == false && etf(right) == true
            // (=>) right > m

            // right - m + 1 <= right - left => recursion isn't infinite
            return discreteRecursiveBinarySearch(thresholdFunction, m, right);
        }
    }

}
