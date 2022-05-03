;(ns expression)

(defn constant [value] (fn [vars] value))
(defn variable [name] (fn [vars] (vars name)))

(defn reduction-op [op] (fn [& children] (fn [vars] (apply op (mapv #(% vars) children)))))
(def add (reduction-op +))
(def subtract (reduction-op -))
(def multiply (reduction-op *))
(def divide
  (reduction-op #(if (== (count %&) 1)
                   (/ 1.0 (first %&))
                   (reduce (fn [l, r] (/ (double l) (double r))) %&))))
(def negate subtract)
(defn exp-sum [& args] (apply + (mapv #(Math/exp %) args)))
(def sumexp (reduction-op exp-sum))
(def softmax (reduction-op #(/ (Math/exp (first %&)) (apply exp-sum %&))))

(def operators {'+ add, '- subtract, '* multiply, '/ divide,
                'negate negate, 'sumexp sumexp, 'softmax softmax})

(defn parseTokenFunction [token]
  (cond
    (number? token) (constant token)
    (symbol? token) (variable (name token))
    (list? token) (apply (operators (first token)) (mapv parseTokenFunction (drop 1 token)))))
(def parseFunction (comp parseTokenFunction read-string))

(defn -main []
  (println (operators (eval '-)))
  (println {:x 1} {"x" 1})
  (println ({"x" 1} "x"))
  (println ({"x" 1} "y"))
  (println (= {:x 1} {"x" 1}))
  (println "====================")
  (println (eval '(+ 1 2)))
  (println (eval (read-string "(+ 1 2)")))
  (println (read-string "(- (* 2 x) 3)"))
  (println (read-string "x") (type (read-string "x")) (name (read-string "x")))
  (println (add (variable "x") (constant 2.0)))
  (println (parseFunction "(+ x 2.0)"))
  (println (parseFunction "(/ (negate x) 2.0)"))
  (println (type (name 'softmax)))
  (println (#(Math/exp %) 0))
  (println ((sumexp (variable "x")) {"z" 0.0, "x" 0.0, "y" 0.0}))
  ;(println (((eval 'softmax) (constant 1) (constant 2) (constant 3)) {}))
  )
