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

    /**
     * @param array contains non-strictly descending values
     * @param value value to search
     *
     * @return if there exists minimal `i` such that `array[i] <= value`
     * — Some(i)
     * — Otherwise, None
     */
    static Optional<Integer> arrayBinarySearch(int[] array, int value, DiscreteBinarySearchEngine searchEngine) {
        // `(Integer index) -> array[index] <= value` is defined at (-1, array.length) and is non-st. mon. inc.
        // because values in array are && -1 < array.length
        // => this function call satisfies pre-condition of DiscreteBinarySearchEngine.search
        DiscreteBinarySearchResult result =
            searchEngine.search((Integer index) -> array[index] <= value, -1, array.length);
        // `DiscreteBinarySearchResult result` => result.rightmostFalse + 1 == result.leftmostTrue &&
        // (We'll denote `extendedThresholdFunction` to be `extendedThresholdFunction` from Engine contract instantiation ↑)
        // extendedThresholdFunction(result.rightmostFalse) == false
        // && extendedThresholdFunction(result.leftmostTrue) == true` &&
        // array contains non-strictly descending values

        if (result.getLeftmostTrue() != array.length) {
            // `DiscreteBinarySearchResult result` => result.rightmostFalse + 1 == result.leftmostTrue &&
            // `extendedThresholdFunction(result.leftmostTrue - 1) == false
            // && extendedThresholdFunction(result.leftmostTrue) == true &&
            // array contains non-strictly descending values &&
            // result.getLeftmostTrue() != array.length
            // (=>) result.leftmostTrue \in [0, array.length)
            // (=>) extendedThresholdFunction(result.leftmostTrue - 1) == false
            // (=>) \forall j \in [0, result.leftmostTrue)
            //      (extendedThresholdFunction(j) == false => array[j] > value)
            // (=>) (extendedThresholdFunction(result.leftmostTrue) == true) => array[result.leftmostTrue] <= value
            return Optional.of(result.getLeftmostTrue());
        } else {
            // `DiscreteBinarySearchResult result` => result.rightmostFalse + 1 == result.leftmostTrue &&
            // extendedThresholdFunction(result.leftmostTrue - 1) == false
            // && extendedThresholdFunction(result.leftmostTrue) == true &&
            // array contains non-strictly descending values &&
            // result.getLeftmostTrue() == array.length
            // (=>) (extendedThresholdFunction(result.leftmostTrue - 1) == false)
            //          => \forall j \in [0, array.length)
            //                  (extendedThresholdFunction(j) == false => array[j] > value)
            // (=>) all cells in array are `< value` => there exist no `i` such that `array[i] <= value`
            return Optional.empty();
        }
    }


    /**
     * @param args Such array that
     * — `Integer.parseInt(args[0])` executes correctly, denote it as `x`.
     * — Integer.parseInt can parse all items from `args[1:]` and collected parse result is denoted as array `a`
     * Prints minimal `i` \in [0, a.length) such that a[i] <= x if any; else — prints `a.length`
     */
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        // — `x` executes correctly, denote it as `x`.
        // — Integer.parseInt can parse all items from `args[1:]` and collected parse result is denoted as array `a`

        int[] a = new int[args.length - 1];
        int i = 0;
        // I: values from args[1..i + 1) are parsed into a[0..i) && i <= args.length - 1
        while (i < args.length - 1) {
            a[i] = Integer.parseInt(args[i + 1]);
            // values from args[1..i + 2) are parsed into a[0..i + 1) && i <= args.length - 1
            i++;
            // values from args[1..i + 1) are parsed into a[0..i) (=> invariant) && i <= args.length - 1
        }
        // i == args.length - 1 => a[0..array.length) array `a` contains parsed args[1:]
        // Prints minimal `i` \in [0, a.length) such that a[i] <= x if any; else — prints `a.length`

        // a is non-str. mon. inc. => correct calls
        var iterativeSearched = arrayBinarySearch(a, x, BinarySearch::discreteIterativeBinarySearch);
        var recursiveSearched = arrayBinarySearch(a, x, BinarySearch::discreteRecursiveBinarySearch);
        // both `iterativeSearched` and `recursiveSearched` ==
        //  {
        //      if there exists minimal `i` such that `array[i] <= value`
        //      * — Some(i)
        //      * — Otherwise, None
        //  }

        if (!iterativeSearched.equals(recursiveSearched)) {
            throw new AssertionError("Iterative and recursive searches gave different results");
        }

        if (iterativeSearched.isPresent()) {
            // there exists minimal `i` such that `array[i] <= value`
            System.out.println(iterativeSearched.get());
        } else {
            // there don't exist minimal `i` such that `array[i] <= value`
            System.out.println(a.length);
        }
    }
}