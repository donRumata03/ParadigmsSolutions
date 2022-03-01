package search;

import java.util.Arrays;
import java.util.Optional;

/**
 * Some useful definitions are provided here…
 *
 * Borrow the definitions of strictly and non-strictly monotonous for real-valued, integer-*
 * and boolean-* (consider false === `0`; true === `1`, for which there exists ordering) functions
 * from Discrete Maths. Abbreviations: SMI, NSMI, SMD, NSMD
 *
 * Integer intervals are denoted as [a, b)
 *
 * Denote function restriction to [a, b) as f|          or just `f|[a, b)`
 *                                           |[a, b)
 *
 * For an array `a` we'll call `a.length` as just «len».
 *
 * We'll call array `a` Strictly Decreasing-Increasing Decayable (SDID)
 * if there exists `m \in [0; len]` such that
 * — \forall i < j in [0, m): a[i] > a[j]
 * — \forall i < j in [m, len): a[i] < a[j]
 * Such `i`s are called «splitters» of `a`.
 *
 * Theorems for `SDID a`, call `len = a.length` :
 * Let g: [0, len) -> Bool, g(i) = `m == len - 1 || a[m] < a[m + 1]`
 *
 * Theorem S1: `g(m)` <==> a[m:r) is SMI.
 *
 * Proof: If first condition is true, conclusion is obvious.
 * Otherwise, we'll proof that g(i + 1) is true (=> by induction, conclusion becomes true)
 *
 * Indeed, for m + 2 == r it's true, and if f(m) < f(m + 1) >= f(m + 2), f wouldn't be SDID,
 * because any splitter would be m + 1 >= s >= m + 2 => contradiction.
 *
 * Backwards proof is obvious.
 * ____________________________________________________________________________________________________
 * Theorem S2: Boolean function g(i) is NSMI
 *
 * Indeed, g(i) <=> f|[i, r) is SMI, so, viewing max SMI postfix [l', r),
 * g(i) is false for i \in [l, l') and true for i \in [l', r)
 */
public class BinarySearchUni {

    /**
     * @param array SDID
     * @return the smallest splitter of `array`
     */
    static int minimalPrefixBeforeIncreasing(int[] array, DiscreteBinarySearchEngine engine) {
        // predicate `(int index) -> index == array.length - 1 || array[index] < array[index + 1]`
        // is `g` function from Theorems S1, S2. It's NSMI and is <==> `f|[m, r)` is SMI.
        // So, wee can find rightmost false, leftmost true. Let's proof that leftmost true of g is the answer.
        // First, for empty arrays, answer is 0, which is OK.
        // Otherwise, g(len - 1) is true, so leftmost true (call it `s`) \in [l, r) and points to such element that:
        // (`f|[s, r)` is SMI) && (`f|[i, r)` is NOT SMI for any i \in [l, s))
        // Firstly, there are no f's splitters \in [l, s) (because right part wouldn't be SMI).
        // Contrary proof that s is splitter:
        // …, but there exist splitters > s (because f is SDID), call one of them t (for which [l, t) is SMD).
        // But prefix [l, s) would be SMD, too => contradiction
        var searched = engine.search(
            (int index) -> index == array.length - 1 || array[index] < array[index + 1],
            -1, array.length
            );
        // searched.leftmostTrue == the smallest splitter of `array`

        return searched.getLeftmostTrue();
    }

    /**
     * @param args Such array that
     * — Integer.parseInt can parse all items from `args` and collected parse result is denoted as array `a`
     * — `a` is SDID
     *
     * Prints the smallest splitter of `a`.
     * (a.k.a. minimal possible length of first such array, formalization of this property)
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
