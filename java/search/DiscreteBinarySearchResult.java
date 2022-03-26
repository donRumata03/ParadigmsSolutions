package search;

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
