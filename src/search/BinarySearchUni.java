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

        var searched = engine.search(
            (Integer index) -> index == array.length - 1 || array[index] < array[index + 1],
            -1, array.length
            );
        // searched == minimal position from which second array can start

        return searched.getLeftmostTrue();
    }

    /**
     * @param args Such array that
     * — Integer.parseInt can parse all items from `args` and collected parse result is denoted as array `a`
     * - `array` can be produced by concatenation of strictly decreasing and strictly increasing array
     * Prints minimal possible length of first such array
     * <==> Prints minimal `i` \in [0, a.length) such that a[i] <= x if any; else — prints `a.length`
     */
    public static void main(String[] args) {
        int[] a = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();

        var iterativeSearched = minimalPrefixBeforeIncreasing(a, DiscreteIterativeBinarySearch::discreteIterativeBinarySearch);

        System.out.println(iterativeSearched);

//        if (!iterativeSearched.equals(recursiveSearched)) {
//            throw new AssertionError("Iterative and recursive searches gave different results");
//        }

//        if (iterativeSearched.isPresent()) {
//            // there exists minimal `i` such that `array[i] <= value`
//            System.out.println(iterativeSearched.get());
//        } else {
//            // there don't exist minimal `i` such that `array[i] <= value`
//            System.out.println(a.length);
//        }
    }
}
