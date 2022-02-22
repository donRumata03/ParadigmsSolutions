package search;

import java.util.Arrays;
import java.util.Optional;

/**
 * Some useful definitions are provided here…
 *
 * Borrow the definitions of strictly and non-strictly monotonous for real-valued, integer-*
 * and boolean-* (consider false === \mathbb{0}; true === \mathbb{1}, for which there's ordering) functions
 * from Discrete Maths. Abbreviations: SMI, NSMI, SMD, NSMD
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
 * Let g: [l, r) -> Bool, g(i) = `m == r - 1 || f(m) < f(m + 1)`
 * Theorem S1: If `g(m)`, then f|[m, r) is SMI and backwards.
 *
 * If first condition is true, conclusion is obvious.
 * If it's the second one but not the first, we'll proof that g(i + 1) is true (=> by induction, conclusion becomes true)
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
        // let len = array.length()
        // let f be [0, len), f(i) = array[i]
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
