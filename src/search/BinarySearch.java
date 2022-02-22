package search;

import java.util.Optional;
import java.util.function.Function;


public class BinarySearch {




    /**
     * @param array is NSMD
     * @param value value to search
     *
     * @return if there exists minimal `i` such that `array[i] <= value`
     * — Some(i)
     * — Otherwise, None
     */
    static Optional<Integer> arrayBinarySearch(int[] array, int value, DiscreteBinarySearchEngine searchEngine) {
        // `(Integer index) -> array[index] <= value` is defined at (-1, array.length) and is NSMI.
        // because values in array are && -1 < array.length
        // => this function call satisfies pre-condition of DiscreteBinarySearchEngine.search
        DiscreteBinarySearchResult result =
            searchEngine.search((int index) -> array[index] <= value, -1, array.length);
        // `DiscreteBinarySearchResult result` => result.rightmostFalse + 1 == result.leftmostTrue &&
        // (We'll denote `etf` to be `etf` from Engine contract instantiation ↑)
        // etf(result.rightmostFalse) == false
        // && etf(result.leftmostTrue) == true` &&
        // array is NSMD

        if (result.getLeftmostTrue() != array.length) {
            // `DiscreteBinarySearchResult result` => result.rightmostFalse + 1 == result.leftmostTrue &&
            // `etf(result.leftmostTrue - 1) == false
            // && etf(result.leftmostTrue) == true &&
            // array is NSMD &&
            // result.getLeftmostTrue() != array.length
            // (=>) result.leftmostTrue \in [0, array.length)
            // (=>) etf(result.leftmostTrue - 1) == false
            // (=>) \forall j \in [0, result.leftmostTrue)
            //      (etf(j) == false => array[j] > value)
            // (=>) (etf(result.leftmostTrue) == true) => array[result.leftmostTrue] <= value
            return Optional.of(result.getLeftmostTrue());
        } else {
            // `DiscreteBinarySearchResult result` => result.rightmostFalse + 1 == result.leftmostTrue &&
            // etf(result.leftmostTrue - 1) == false
            // && etf(result.leftmostTrue) == true &&
            // array is NSMD &&
            // result.getLeftmostTrue() == array.length
            // (=>) (etf(result.leftmostTrue - 1) == false)
            //          => \forall j \in [0, array.length)
            //                  (etf(j) == false => array[j] > value)
            // (=>) all cells in array are `< value` => there exist no `i` such that `array[i] <= value`
            return Optional.empty();
        }
    }


    /**
     * @param args Such array that
     * — `Integer.parseInt(args[0])` executes correctly, denote it as `x`.
     * — Integer.parseInt can parse all items from `args[1:]` and collected parse result is denoted as array `a`
     * - And `a` is NSMD
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

        // a is NSMD => correct calls
        var iterativeSearched =
            arrayBinarySearch(a, x, DiscreteIterativeBinarySearch::discreteIterativeBinarySearch);
        var recursiveSearched =
            arrayBinarySearch(a, x, DiscreteRecursiveBinarySearch::discreteRecursiveBinarySearch);
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
