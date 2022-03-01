package queue;



/**
 * Model: infinite sequence a[0..+inf], integer L, integer R
 * Invariant: for i \in [L, R): a[i] != null
 *
 * Let immutable(l, r): for i \in [l, r): a'[i] == a[i]
 * Let `cL`: L' == L
 * Let `cR`: R' == R
 * Let `nE`: R - L > 0
 */
public interface Queue {
    /**
     * Pred: true
     * Post:
     *  — Constructs queue with 0 <= L = R
     */
    // Implied Default Constructor

    /**
     * Pred: element != null
     * Post:
     *  — immutable(L,  R)
     *  — a[R] = element
     *  — R' = R + 1
     *  — cL
     */
    void enqueue(final Object element);

    /**
     * Pred: nE
     * Post:
     *  — immutable(L + 1,  R)
     *  — L' = L + 1
     *  — cR
     *  — Ret = a[L]
     */
    Object dequeue();

    /**
     * Pred: true
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = R - L
     */
    int size();

    /**
     * Pred: true
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = (R - L == 0)
     */
    boolean isEmpty();

    /**
     * Pred: true
     * Post:
     *  — 0 <= L = R
     */
    void clear();

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = a[L]
     */
    Object element();

    /**
     * Pred: element != null
     * Post:
     *  — immutable(L,  R)
     *  — cR
     *  — L' = L - 1
     *  — a[L'] = element
     */
    void push(final Object element);

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = a[R - 1]
     */
    Object peek();

    /**
     * Pred: nE
     * Post:
     *  — immutable(L,  R - 1)
     *  — cL
     *  — R' = R - 1
     *  — Ret = a[R - 1]
     */
    Object remove();


    /**
     * Pred: true
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = if there's some — minimal such index `i` \in [0, R - L) that a[L + i] == element,
     *    Otherwise: `-1`
     */
    int indexOf(Object element);

    /**
     * Pred: true
     * Post:
     *  — immutable(L,  R)
     *  — cL
     *  — cR
     *  — Ret = if there's some — maximal such index `i` \in [0, R - L) that a[L + i] == element,
     *    Otherwise: `-1`
     */
    int lastIndexOf(Object element);
}
