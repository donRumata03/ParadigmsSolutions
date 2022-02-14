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

    /**
     * @param left
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
     * @param array contains non-strictly descending values
     * @param value value to search
     *
     * @return if there exists minimal `i` such that `array[i] <= value`
     * — Some(i)
     * — Otherwise, None
     */
    static Optional<Integer> arrayBinarySearch(int[] array, int value) {
        DiscreteBinarySearchResult res =
            discreteIterativeBinarySearch((Integer index) -> array[index] <= value, -1, array.length);

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

        var searched = arrayBinarySearch(a, x);

        if (searched.isPresent()) {
            System.out.println(searched.get());
        } else {
//            System.out.println("Fuck you!");
            System.out.println(a.length);
        }
    }
}
