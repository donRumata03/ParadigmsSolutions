package search;

import java.util.Optional;
import java.util.function.Function;

/**
 *  Invariant: `rightmostFalse + 1 == leftmostTrue`
 */
class DiscreteBinarySearchResult {
    private final int rightmostFalse;
    private final int leftmostTrue;

    public DiscreteBinarySearchResult(int rightmostFalse, int leftmostTrue) {
        assert rightmostFalse + 1 == leftmostTrue;

        this.rightmostFalse = rightmostFalse;
        this.leftmostTrue = leftmostTrue;
    }

    public int getRightmostFalse() {
        return rightmostFalse;
    }

    public int getLeftmostTrue() {
        return leftmostTrue;
    }
}

public class BinarySearch {

    // Pre- and post- conditions for binary search:

    @FunctionalInterface
    interface DiscreteBinarySearchEngine {
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
        DiscreteBinarySearchResult search(Function<Integer, Boolean> thresholdFunction, int left, int right);
    }


    /**
     * Satisfies «Pre- and post- conditions for binary search» ↑↑↑
     */
    static DiscreteBinarySearchResult discreteIterativeBinarySearch(Function<Integer, Boolean> thresholdFunction, int left, int right) {
        int l = left;
        int r = right;

        // Invariant:
        // extendedThresholdFunction non-strictly monotonously increase at [l, r] &&
        // extendedThresholdFunction(l) == false && extendedThresholdFunction(r) == true &&
        // l < r

        while (l + 1 != r) {
            int m = (l + r) / 2;

            if (thresholdFunction.apply(m)) {
                r = m;
            } else {
                l = m;
            }
        }

        return new DiscreteBinarySearchResult(l, r);
    }

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

            // right - m + 1 <= right - left => recursion isn't infinite
            return discreteRecursiveBinarySearch(thresholdFunction, m, right);
        }
    }

    /**
     * @param array contains non-strictly descending values
     * @param value value to search
     *
     * @return if there exists minimal `i` such that `array[i] <= value`
     * — Some(i)
     * — Otherwise, None
     */
    static Optional<Integer> arrayBinarySearch(int[] array, int value, DiscreteBinarySearchEngine searchEngine) {
        // Note that this function call satisfies pre-condition discreteIterativeBinarySearch
        DiscreteBinarySearchResult res =
            searchEngine.search((Integer index) -> array[index] <= value, -1, array.length);

        return res.getLeftmostTrue() != array.length ?
            Optional.of(res.getLeftmostTrue()) :
            Optional.empty();
    }


    /**
     * @param args Such array that
     * — `Integer.parseInt(args[0])` executes correctly, denote it as `x`.
     * — Integer.parseInt can parse all items from `args[1:]` and collected parse result is denoted as array `a`
     * Denote `a.size()` as `len`
     * Prints minimal `i` \in [0, len) such that arg[i]
     */
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);

        int[] a = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }

        var iterativeSearched = arrayBinarySearch(a, x, BinarySearch::discreteIterativeBinarySearch);
        var recursiveSearched = arrayBinarySearch(a, x, BinarySearch::discreteRecursiveBinarySearch);

        if (!iterativeSearched.equals(recursiveSearched)) {
            throw new AssertionError("Iterative and recursive searches gave different results");
        }

        if (iterativeSearched.isPresent()) {
            System.out.println(iterativeSearched.get());
        } else {
//            System.out.println("Fuck you!");
            System.out.println(a.length);
        }
    }
}
