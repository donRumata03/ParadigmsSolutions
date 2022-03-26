package search;

import java.util.function.IntPredicate;

public class DiscreteIterativeBinarySearch {
    /**
     * Satisfies «Pre- and post- conditions for binary search» ↑↑↑
     */
    static DiscreteBinarySearchResult discreteIterativeBinarySearch(IntPredicate thresholdFunction, int left, int right) {
        int l = left;
        int r = right;

        // Invariant (we get it from contract by denoting left as l, right as r):
        // etf NSMI at [l, r] &&
        // etf(l) == false && etf(r) == true &&
        // l < r

        // `r - l` decreases at each iteration at least by 1 => loop isn't infinite
        while (l + 1 != r) {
            // etf NSMI at [l, r] &&
            // etf(l) == false && etf(r) == true &&
            // l + 1 < r
            int m = (l + r) / 2;
            // etf NSMI at [l, r] &&
            // etf(l) == false && etf(r) == true &&
            // l + 1 < r &&
            // m - l >= 1 &&
            // r - m >= 1 &&
            // (=>) m \in (left, right)


            if (thresholdFunction.test(m)) {
                // etf NSMI at [l, r] &&
                // etf(l) == false && etf(r) == true &&
                // l + 1 < r &&
                // m \in (left, right) &&
                // etf(m) == true
                r = m;
                // etf NSMI at [l, r] &&
                // etf(l) == false && etf(r) == true &&
                // l < r
            } else {
                // etf NSMI at [l, r] &&
                // etf(l) == false && etf(r) == true &&
                // l + 1 < r &&
                // m \in (left, right) &&
                // etf(m) == true
                l = m;
                // etf NSMI at [l, r] &&
                // etf(l) == false && etf(r) == true &&
                // l < r
            }

            // etf NSMI at [l, r] &&
            // etf(l) == false && etf(r) == true &&
            // l < r
        }

        // etf non-strictly monotonously increase at [l, r] &&
        // etf(l) == false && etf(r) == true &&
        // l < r &&
        // r == l + 1 (=> constructing is correct)
        return new DiscreteBinarySearchResult(l, r);
    }

}
