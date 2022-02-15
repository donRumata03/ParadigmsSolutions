package search;

import java.util.Arrays;
import java.util.Optional;

public class BinarySearchUni {

    /**
     *
     * @param array is uni-variate (can be produced by concatenation of strictly decreasing and strictly increasing arrays)
     * @return minimal size of such first array
     */
    static int minimalPrefixBeforeIncreasing(int[] array, DiscreteBinarySearchEngine engine) {
        // predicate `(Integer index) -> index == array.length - 1 || array[index] < array[index + 1]`
        // checks whether there are positions in `[0, index]` that can be the start of second array
        // (
        //  via extendedThresholdFunction indexes < 0 and >= array.length
        //  are considered to have false and true correspondingly
        // )
        // proof: if index == array.length - 1, than by contract it's true
        // otherwise it exists in somewhere in array but can't be after index => it's in [0, index]
        // we get maximal index for which
        var searched = engine.search(
            (Integer index) -> index == array.length - 1 || array[index] < array[index + 1],
            -1, array.length
            );
        // searched.leftmostTrue == minimal position from which second array can start

        return searched.getLeftmostTrue();
    }

    /**
     * @param args Such array that
     * — Integer.parseInt can parse all items from `args` and collected parse result is denoted as array `a`
     * - `array` can be produced by concatenation of strictly decreasing and strictly increasing array
     * Prints minimal possible length of first such array
     */
    public static void main(String[] args) {
        int[] a = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
        // => `a` is array from contract

        var iterativeSearched = minimalPrefixBeforeIncreasing(a, DiscreteIterativeBinarySearch::discreteIterativeBinarySearch);
        var recursiveSearched = minimalPrefixBeforeIncreasing(a, DiscreteRecursiveBinarySearch::discreteRecursiveBinarySearch);
        // They both by post-condition are minimal possible lengths of first such array

        if (iterativeSearched != recursiveSearched) {
            // Impossible
            throw new AssertionError("Iterative and recursive searches gave different results");
        }

        System.out.println(iterativeSearched);
    }
}
