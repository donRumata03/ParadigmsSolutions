package queue;


import java.util.function.Predicate;

/**
 * Model: infinite sequence a[0..+inf], integer L, integer R
 * Invariant: for i \in [L, R): a[i] != null
 *
 * Let immutable(l, r): for i \in [l, r): a'[i] == a[i]
 * Let `cL`: L' == L
 * Let `cR`: R' == R
 * Let `nE`: R - L > 0
 *
 * Let remove(index_set):
 * - Denote prefix sum of booleans[elements deleted in `[0, i]` ]  as `p`
 * - cL
 * - R' = R - p[R - 1]
 * - for i \in [L, R'): a'[i] = a[i + p[i]]
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

    /**
     * Pred: true
     * Post: remove(such `i` in [L, R) that predicate(i))
     */
    void removeIf(Predicate<Object> predicate);

    /**
     * Pred: true
     * Post: remove(such `i` in [L, R) that !predicate(i))
     */
    void retainIf(Predicate<Object> predicate);

    /**
     * Pred: true
     * Post: remove(such `i` in [L, R) that there exists j \in [L, i] such that !predicate(j))
     */
    void takeWhile(Predicate<Object> predicate);

    /**
     * Pred: true
     * Post: remove(such `i` in [L, R) that there DOESN'T exist j \in [L, i] such that !predicate(j))
     */
    void dropWhile(Predicate<Object> predicate);
}
