(ns linear (:require [clojure.test :refer [deftest is run-tests]]))


(defn isSuffixOf
  [suffix, origin]
  (let [
        lSuff (count suffix)
        lOrigin (count origin)
        ]
    (= suffix (drop (- lOrigin lSuff) origin))
    )
  )

(defn same-deductible-property [deducer & vs] (and (not-empty vs) (let [prop (deducer (nth vs 0))]
                                                                    (every? #(= (deducer %) prop) vs)))
  )

(defn same-deductible-property-seq [deducer vs-seq] (apply (partial same-deductible-property deducer) vs-seq))

(defn same-property [deducer & vs] (or (empty? vs) (same-deductible-property deducer vs))
  )
(defn same-property-seq [deducer vs-seq] (apply (partial same-property deducer) vs-seq))


(defn matrix-dimension
  [m] [(count m) (if (empty? m) 1 (count (nth m 0)))]
  )


(defn tensor-dimension [t]
  (cond
    (number? t) (list )
    (vector? t) (let [childResult (tensor-dimension (first t))]
                  (if (nil? childResult)
                    nil
                    (if (empty? t) (list 0) (cons (count t) childResult))
                    )
                  )
    :else nil
    )
  )

(defn check-is-tensor-of-dimension
  [t, dim]
  (if (nil? dim)
    false
    (if (= dim (list ))
      (number? t)
      (and
        (vector? t)
        (= (count t) (first dim))
        (every? #(check-is-tensor-of-dimension % (rest dim)) t)
        )
      )
    )
  )

(defn broadcastTensor
  [t, additionalDims]
  (if (empty? additionalDims)
    t
    (vector (repeat
              (first additionalDims)
              (broadcastTensor t (rest additionalDims))
              ))
    )
  )

(deftest tDim
  (is (= (tensor-dimension 30) (list )))
  (is (= (tensor-dimension []) (list 0)))
  (is (= (tensor-dimension [[566]]) (list 1 1)))
  (is (= (tensor-dimension [[[]] [[]]]) (list 2 1 0)))
  (is (= (tensor-dimension [[[2 3 4] [5 6 7]]]) (list 1 2 3)))
   )

(deftest tIncorrect
  (is (= (tensor-dimension nil) nil))
  (is (= (tensor-dimension [(fn[] )]) nil))
  )

(defn number-matrix? [m] (and
                           (vector? m)
                           (every? vector? m)
                           (same-property-seq count m)
                           (every? (partial every? number?) m)
))
(defn number-vector? [v] (and (vector? v) (every? number? v)))
(defn number-matrix-of? [m dim] (and (number-matrix? m) (= dim (matrix-dimension m))))
(defn number-vector-of? [v size] (and (number-vector? v) (= size (count v))))

(defn matrices-match
  [ml mr] (let [dl (matrix-dimension ml), dr (matrix-dimension mr)]
    (= (nth dl 1) (nth dr 0)))
  )

(defn same-size-number-vector-set [& vs]
  (and (every? number-vector? vs) (same-deductible-property-seq count vs))
  )


(defn same-size-matrix-set [& ms]
  (and (every? number-matrix? ms) (same-deductible-property-seq matrix-dimension ms))
  )


(defn vectorCoordWiseOperation [op]
  (fn [& vs]
    (vec (for [i (range(count (first vs)))]
    (apply op (map #(nth % i) vs))))))

(defn numberVectorCoordWiseOperation [op]
  (let [unconstrained (vectorCoordWiseOperation op)]
    (fn [& vs]  {:pre [(apply same-size-number-vector-set vs)]} (apply unconstrained vs))
  )
)

(defn matrixElementWiseOperation [op]
  (fn [& ms] {:pre [(apply same-size-matrix-set ms)]}
    (apply (vectorCoordWiseOperation (numberVectorCoordWiseOperation op)) ms)))

(defn foldify
  [f neutral] (fn [& args] (reduce f neutral args))
  )

(defn reductify
  [f] (fn [& args] {:pre [(not-empty args)]} (reduce f args))
  )


(def v+ (numberVectorCoordWiseOperation +))
(def v- (numberVectorCoordWiseOperation -))
(def v* (numberVectorCoordWiseOperation *))
(defn v*s [v & ss] {:pre [(every? number? ss) (number-vector? v)]}
  (let [product (reduce * 1 ss)]
    (mapv #(* product %) v)))
(def vd (numberVectorCoordWiseOperation /))

(def m+ (matrixElementWiseOperation +))
(def m- (matrixElementWiseOperation -))
(def m* (matrixElementWiseOperation *))
(defn m*s [m & ss] {:pre [(every? number? ss) (number-matrix? m)]}
  (let [product (reduce * 1 ss)]
    (mapv #(mapv (fn [row] (* product row)) %) m)))
(def md (matrixElementWiseOperation /))


(defn vect2 [a b] {:pre [(number-vector? a), (number-vector? b), (= (count a) 3), (= (count b) 3)]}
  [
   (- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
   (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
   (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))
   ]
  )

(def vect (reductify vect2))

(defn scalar [& vs] {:pre [(or (empty? vs) (apply same-size-number-vector-set vs))]}
  (reduce + 0 (apply v* vs))
  )

(defn transpose [m] {:pre [(number-matrix? m)]}
  (if (empty? m)
    [[]]
    (vec (for [col (range (count (nth m 0)))]
           (vec (for [row (range (count m))]
                  ((m row) col)))))
    )
  )

(defn row*m [row m] {:pre [(number-vector? row), (number-matrix? m), (= (count row) (nth (matrix-dimension m) 0))]}
  (let [transposed (transpose m)] (mapv #(scalar row %) transposed))
  )

(defn m*m_two [ml mr] {:pre [(number-matrix? ml), (number-matrix? mr), (matrices-match ml mr)]}
  (mapv #(row*m % mr) ml)
  )

(def m*m
  (reductify m*m_two)
  )

(defn m*v [m v] {:pre [(number-matrix? m), (number-vector? v), (= (nth (matrix-dimension m) 1) (count v))]}
  ((transpose (m*m m (transpose [v]))) 0)
  )

(defn -main []
  (println (same-size-number-vector-set [100 1] [15 6] [0 0]))
  (println (same-size-matrix-set [[1 2] [3 4]] [[5 6] [7 8]]))
  (println (v+ [] []))
  (println (matrix-dimension []))
  (println (v+ [1 2] [3 4] [5 6]))
  (println (v* [1 2] [3 4]))
  (println "==========")
  (println ((vectorCoordWiseOperation (numberVectorCoordWiseOperation +)) [[1 2] [3 4]] [[5 6] [7 8]]))
  (println (m+ [[1 2] [3 4]] [[5 6] [7 8]]))
  (println (vect [1 2 3] [4 5 6]))
  (println (scalar [1 2 3] [4 5 6]))
  (println (transpose [[1 2 3] [4 5 6]]))
  (println (transpose []))
  (println (v*s [1 2 3] 1 2 3))
  (println (m*s [[1 2 3] [4 5 6]] 1 2 3))
  (println (row*m [7 8] [[1 2 3] [4 5 6]]))
  (println (m*m [[7 8 9] [10 11 12] [13 14 15]] [[1 2] [3 4] [5 6]]))
  (println (m*v [[7 8 9] [10 11 12] [13 14 15]] [1 2 3]))
  (println (m*m [[]] []))
  (println (m*v [[]] []))
  (println (v- [8.3]))
  (println (m*m (vector (vector 0.8))))
  (println "========")
  (println (number-vector? [1 2 3]))
  (println (number-vector? 1.0))
  (println (number-vector-of? [1 2 3] 3))
  (println (number-vector-of? [1 2 3] 0))
  (println "========")
  (println (number-matrix? 5))
  (println (number-matrix? [[1] 2 [3]]))
  (println (number-matrix? [[1 2 3] [1 2 3]]))
  (println (number-matrix-of? [[1 2 3] [1 2 3]] [2 3]))
  (println (number-matrix-of? [[1 2 3] [1 2 3]] [3 2]))
  (println "========")
  (println (same-size-number-vector-set [] []))
  (println "========")
  (println (tensor-dimension 1))
  )