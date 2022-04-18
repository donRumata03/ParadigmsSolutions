;(ns linear)

(defn same-deductible-property [deducer & vs] (and (not-empty vs) (let [prop (deducer (nth vs 0))]
                                                                    (every? #(= (deducer %) prop) vs)))
  )

(defn same-property [deducer & vs] (or (empty? vs) (same-deductible-property deducer vs))
  )

(def same-size-vector-set
  (partial same-deductible-property count))

(defn matrix-dimension
  [m] [(count m) (if (empty? m) 1 (count (nth m 0)))]
  )

(def number-matrix? (partial same-property count))
(def number-vector? (partial same-property count))
(defn number-matrix-of? [m dim] (and (number-matrix? m) (= dim (matrix-dimension m))))
(defn number-vector-of? [v size] (and (number-vector? v) (= size (count v))))

(defn matrices-match
  [ml mr] (let [dl (matrix-dimension ml), dr (matrix-dimension mr)]
    (= (nth dl 1) (nth dr 0)))
  )

(def same-size-matrix-set
  (partial same-deductible-property matrix-dimension))

(defn vectorCoordWiseOperation [op]
  (fn [& vs]  {:pre [(apply same-size-vector-set vs)]}
    (vec (for [i (range(count (nth vs 0)))]
    (apply op (map #(nth % i) vs))))))

(defn matrixElementWiseOperation [op]
  (fn [& ms] {:pre [(same-size-matrix-set ms), (every? number-matrix? ms)]} (apply (vectorCoordWiseOperation (vectorCoordWiseOperation op)) ms)))

(defn foldify
  [f neutral] (fn [& args] (reduce f neutral args))
  )

(defn reductify
  [f] (fn [& args] {:pre [(not-empty args)]} (reduce f args))
  )


(def v+ (vectorCoordWiseOperation +))
(def v- (vectorCoordWiseOperation -))
(def v* (vectorCoordWiseOperation *))
(defn v*s [v & ss]
  (let [product (reduce * 1 ss)]
    (mapv #(* product %) v)))
(def vd (vectorCoordWiseOperation /))

(def m+ (matrixElementWiseOperation +))
(def m- (matrixElementWiseOperation -))
(def m* (matrixElementWiseOperation *))
(defn m*s [m & ss]
  (let [product (reduce * 1 ss)]
    (mapv #(mapv (fn [row] (* product row)) %) m)))
(def md (matrixElementWiseOperation /))


(defn vect2 [a b]
  [
   (- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
   (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
   (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))
   ]
  )

(def vect (reductify vect2))

(defn scalar
  [& vs] (reduce + 0 (apply v* vs))
  )

(defn transpose
  [m] (if (empty? m)
    [[]]
    (vec (for [col (range (count (nth m 0)))]
           (vec (for [row (range (count m))]
                  ((m row) col)))))
    )
  )

(defn row*m
  [row m] {:pre [(= (count row) (nth (matrix-dimension m) 0))]} (let [transposed (transpose m)] (mapv #(scalar row %) transposed))
  )

(defn m*m_two
  [ml mr] {:pre [(matrices-match ml mr)]} (mapv #(row*m % mr) ml)
  )

(def m*m
  (reductify m*m_two)
  )

(defn m*v
  [m v] ((transpose (m*m m (transpose [v]))) 0)
  )

(defn -main []
  (println "==========")
  (println (same-size-vector-set [100 1] [15 6] [0 0]))
  (println (v+ [] []))
  (println (matrix-dimension []))
  (println (v+ [1 2] [3 4] [5 6]))
  ;(println (v* [1 2] [3 4]))
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
  )

