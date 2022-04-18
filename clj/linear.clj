(ns linear)

(defn vectorCoordWiseOperation [op]
  (fn [& vs] (reduce #(mapv op %1 %2) vs)))

(defn matrixElementWiseOperation [op]
  (fn [& ms] (apply (vectorCoordWiseOperation (vectorCoordWiseOperation op)) ms)))

(defn foldify
  [f, neutral] (fn [& args] (reduce f neutral args))
  )

(defn reductify
  [f] (fn [& args] (reduce f args))
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


(defn vect2 [a, b]
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
  [m] (vec (for [col (range (count (nth m 0)))]
      (vec (for [row (range (count m))]
        ((m row) col)))))
  )

(defn row*m
  [row m] (let [transposed (transpose m)] (mapv #(scalar row %) transposed))
  )


(defn -main []
  (println)
  (println (== 1 1.0))
  (def x 10)
  (println (+ x (* x 3)))
  (println "==========")

  (println (vector 1 2 "Hello" 3 4))
  (println (list 1 2 "Hello" 3 4))
  (println (loop [i 0]
    (if (< i 10)
      (recur (inc i))
      i)))
  ;
  (println (v+ [1 2] [3 4] [5 6]))
  ;(println (v* [1 2] [3 4]))
  (println (m+ [[1 2] [3 4]] [[5 6] [7 8]]))
  (println (vect [1 2 3] [4 5 6]))
  (println (scalar [1 2 3] [4 5 6]))
  (println (transpose [[1 2 3] [4 5 6]]))
  (println (v*s [1 2 3] 1 2 3))
  (println (m*s [[1 2 3] [4 5 6]] 1 2 3))
  (println (row*m [7 8] [[1 2 3] [4 5 6]]))
  )

