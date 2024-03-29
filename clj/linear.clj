;(ns linear (:require [clojure.test :refer [deftest is run-tests]]))

(defn zip [& colls] (partition (count colls) (apply interleave colls)))

(defn is-suffix-of [suffix, origin]
  (let [lSuff (count suffix)
        lOrigin (count origin)]
    (= suffix (drop (- lOrigin lSuff) origin))))

(defn same-property [deducer vs-seq] (or (empty? vs-seq) (apply = (map deducer vs-seq))))

(defn same-deductible-property [deducer vs] (and (not-empty vs) (same-property deducer vs)))

(defn matrix-dimension [m] [(count m) (if (empty? m) 1 (count (nth m 0)))])

(def rows count)
(defn cols [m] (if (empty? m) 1 (count (first m))))

(defn tensor-dimension [t]
  (cond
    (number? t) (list )
    (vector? t) (some-> (tensor-dimension (first t))
                        (#(if (empty? t) (list 0) (cons (count t) %))))
    :else nil))

(defn tensor-of-dimension? [t, dim]
  (if (nil? dim)
    false
    (if (= dim (list ))
      (number? t)
      (and
        (vector? t)
        (= (count t) (first dim))
        (every? #(tensor-of-dimension? % (rest dim)) t)))))

(defn broadcastTensorWith
  [t, additionalDims]
  (if (empty? additionalDims) t
    (apply vector (repeat (first additionalDims)
              (broadcastTensorWith t (rest additionalDims))))))

(defn broadcastTensorTo [t, newDim]
  (broadcastTensorWith t (take (- (count newDim) (count (tensor-dimension t))) newDim)))

(defn number-tensor? [t] (tensor-of-dimension? t (tensor-dimension t)))

(defn number-matrix? [m]
  (and (vector? m)
    (every? vector? m)
    (same-property count m)
    (every? (partial every? number?) m)))

(defn number-vector? [v] (and (vector? v) (every? number? v)))
(defn number-matrix-of? [m dim] (and (number-matrix? m) (= dim (matrix-dimension m))))
(defn number-vector-of? [v size] (and (number-vector? v) (= size (count v))))

(defn same-size-number-vector-set [& vs]
  (and (every? number-vector? vs) (same-deductible-property count vs)))

(defn same-size-matrix-set [& ms]
  (and (every? number-matrix? ms) (same-deductible-property matrix-dimension ms)))

(defn vectorCoordWiseOperation [op]
  (fn [& vs]
    (vec (for [i (range(count (first vs)))]
    (apply op (map #(nth % i) vs))))))

(defn numberVectorCoordWiseOperation [op]
  (let [unconstrained (vectorCoordWiseOperation op)]
    (fn [& vs]  {:pre [(apply same-size-number-vector-set vs)] :post [(apply same-size-number-vector-set % vs)]} (apply unconstrained vs))
  )
)

(defn matrixElementWiseOperation [op]
  (fn [& ms] {:pre [(apply same-size-matrix-set ms)]}
    (apply (vectorCoordWiseOperation (numberVectorCoordWiseOperation op)) ms)))

(defn foldify [f neutral] (fn [& args] (reduce f neutral args)))
(defn reductify [f] (fn [& args] {:pre [(not-empty args)]} (reduce f args)))


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

; Tensor element-wise operations
(defn same-size-tensorElementWiseOperation [op]
  (letfn [(operate [& tensors]
    (let [dim (tensor-dimension (first tensors))]
      (if (empty? dim)
        (apply op tensors)
        (mapv (partial apply operate) (apply zip tensors)))))] operate))

(defn biggestDimension [& tensors] (apply max-key count (mapv tensor-dimension tensors)))
(defn auto-broadcast-tensors [& tensors]
  (let [resDim (apply biggestDimension tensors)]
    (mapv #(broadcastTensorTo % resDim) tensors)))

(defn broadcastable-tensor-element-wise-operation [op]
  (let [ssOp (same-size-tensorElementWiseOperation op)]
    (fn [& tensors]
      {:pre [(every? number-tensor? tensors)
             (let [resDim (apply biggestDimension tensors)]
               (every? #(is-suffix-of (tensor-dimension %) resDim) tensors))]
      :post [(number-tensor? %), (= (tensor-dimension %) (apply biggestDimension tensors))]}
      (apply ssOp (apply auto-broadcast-tensors tensors)))))

(def hb+ (broadcastable-tensor-element-wise-operation +))
(def hb- (broadcastable-tensor-element-wise-operation -))
(def hb* (broadcastable-tensor-element-wise-operation *))
(def hbd (broadcastable-tensor-element-wise-operation /))

; Typical linear algebra
(defn vect2 [a b]
  {:pre [(every? #(number-vector-of? % 3) [a b])]
   :post [(number-vector-of? % 3)]}
  [(- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
   (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
   (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))])
(def vect (reductify vect2))

(defn scalar [& vs]
  {:pre [(or (empty? vs) (apply same-size-number-vector-set vs))]
  :post [(number? %)]}
  (reduce + 0 (apply v* vs))
  )

(defn transpose [m]
  {:pre [(number-matrix? m)]
  :post [(number-matrix? %),
         (or (and (= % []) (= 0 (cols m)))
             (= (matrix-dimension %) (reverse (matrix-dimension m))))]}
  (if (empty? m) [[]]
    (vec (for [col (range (cols m))]
           (vec (for [row (range (rows m))]
                  ((m row) col)))))))

(defn row*m [row m]
  {:pre [(number-matrix? m), (number-vector-of? row (rows m))]
   :post [(number-vector-of? % (cols m))]}
  (let [transposed (transpose m)] (mapv #(scalar row %) transposed)))

(defn m*m_two [ml mr]
  {:pre [(number-matrix? ml), (number-matrix? mr), (= (cols ml) (rows mr))]
  :post [(number-matrix? %), (= (matrix-dimension %) (list (first (matrix-dimension ml)) (second (matrix-dimension mr))))]}
  (mapv #(row*m % mr) ml))

(def m*m (reductify m*m_two))

(defn m*v [m v] (first (transpose (m*m m (transpose [v])))))


(defn -main []
  (println (some->> nil (+ 2)))
  (println (some->> 10 (+ 2)))
  (println "===========")

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
  (println "==========")
  (println (v*s [1 2 3] 1 2 3))
  (println (m*s [[1 2 3] [4 5 6]] 1 2 3))
  (println (row*m [7 8] [[1 2 3] [4 5 6]]))
  (println (m*m [[7 8 9] [10 11 12] [13 14 15]] [[1 2] [3 4] [5 6]]))
  (println (m*v [[7 8 9] [10 11 12] [13 14 15]] [1 2 3]))
  (println "==========")
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
  (println "========")
  (println (broadcastTensorWith 1 (list 2 3)))
  (println (broadcastTensorTo 1 (list 2 3)))
  (println (auto-broadcast-tensors 1 [ [10 20 30] [40 50 60] ] [100 200 300]))
  (println (hb+ 1 [ [10 20 30] [40 50 60] ] [100 200 300]))
  (println (transpose (vector (vector) (vector)))) ; [[] []] → []
  )