package search;

import java.util.function.Function;

public class DiscreteIterativeBinarySearch {
    /**
     * Satisfies «Pre- and post- conditions for binary search» ↑↑↑
     */
    static DiscreteBinarySearchResult discreteIterativeBinarySearch(Function<Integer, Boolean> thresholdFunction, int left, int right) {
        int l = left;
        int r = right;

        // Invariant (we get it from contract by denoting left as l, right as r):
        // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
        // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
        // l < r

        // `r - l` decreases at each iteration at least by 1 => loop isn't infinite
        while (l + 1 != r) {
            // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
            // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
            // l + 1 < r
            int m = (l + r) / 2;
            // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
            // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
            // l + 1 < r &&
            // m - l >= 1 &&
            // r - m >= 1 &&
            // (=>) m \in (left, right)


            if (thresholdFunction.apply(m)) {
                // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
                // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
                // l + 1 < r &&
                // m \in (left, right) &&
                // extendedThresholdFunction(m) == true
                r = m;
                // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
                // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
                // l < r
            } else {
                // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
                // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
                // l + 1 < r &&
                // m \in (left, right) &&
                // extendedThresholdFunction(m) == true
                l = m;
                // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
                // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
                // l < r
            }

            // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
            // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
            // l < r
        }

        // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
        // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
        // l < r &&
        // r == l + 1 (=> constructing is correct)
        return new DiscreteBinarySearchResult(l, r);
    }

}
