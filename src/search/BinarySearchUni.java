package search;

import java.util.Arrays;
import java.util.Optional;

/**
 * Some useful definitions are provided here…
 *
 * Borrow the definitions of strictly and non-strictly monotonous for real-valued, integer-*
 * and boolean-* (consider false === \mathbb{0}; true === \mathbb{1}) functions.
 * Abbreviations: SMI, NSMI, SMD, NSMD
 *
 * Integer intervals are denoted as [a, b)
 *
 * Denote function restriction to [a, b) as f|          or just `f|[a, b)`
 *                                           |[a, b)
 *
 * We'll call function `f: [l, r) \subset \mathbb{Z} |-> (T: Ord)`
 * Strictly Decreasing-Increasing Decayable (SDID) if there exists `m \in [l; r]` such that
 * — \forall i < j in [l, m): f(i) > f(j)
 * — \forall i < j in [m, r): f(i) < f(j)
 *
 * For a given `f` the set of such `i`s is denoted as splitters(f). So, `f` is SDID <=> there exists its splitters
 *
 * Array `a` is SDID if `acc: [0, a.length()) |-> decltype(a[0])` such that acc(i) = a[i] is SDID
 *
 * Note that being SDID for an array is the formalization of
 * «being possible to be produced by concatenation of strictly decreasing and strictly increasing arrays (both may be empty)»
 *
 * Theorems for `SDID f: [l, r) |-> T`:
 *
 * Theorem S1: If f(m) < f(m + 1), then f|[m, r) is SMI.
 *
 * Theorem S2: Boolean function g: [l, r - 1) -> Bool, g(i) = (f(i) < f(i + 1)) is NSMI
 *
// * Theorem S3: and for some m \in [l, r): f(m) < f(m + 1), then f|           is SDID.
// *                                                                                          | [l, m]
 *
 *
 */
public class BinarySearchUni {

    /**
     *
     * @param array SDID
     * @return the smallest splitter of `array`
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
     * — `a` is SDID
     * Prints the smallest splitter of `a`. (a.k.a. minimal possible length of first such array)
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
