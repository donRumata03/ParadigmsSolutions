(ns linear)

(defn vectorCoordWiseOperation [op]
  (fn [& vs] (println "vec" vs) (reduce #(mapv op %1 %2) vs)))

(defn matrixElementWiseOperation [op]
  (fn [& ms] (apply (vectorCoordWiseOperation (vectorCoordWiseOperation op)) ms)))

(def v+ (vectorCoordWiseOperation +))
(def v- (vectorCoordWiseOperation -))
(def v* (vectorCoordWiseOperation *))
(def vd (vectorCoordWiseOperation /))

(def m+ (matrixElementWiseOperation +))
(def m- (matrixElementWiseOperation -))
(def m* (matrixElementWiseOperation *))
(def md (matrixElementWiseOperation /))


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
  )

