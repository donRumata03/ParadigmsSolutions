(ns expression)

(load-file "proto.clj")

(defn div ([arg] (/ 1 (double arg)))
  ([first & tail] (reduce (fn [l, r] (/ (double l) (double r))) (apply vector first tail))))

(defn constant [value] (fn [vars] value))
(defn variable [name] (fn [vars] (vars name)))

(defn reduction-op [op] (fn [& children] (fn [vars] (apply op (mapv #(% vars) children)))))
(def add (reduction-op +))
(def subtract (reduction-op -))
(def multiply (reduction-op *))
(def divide (reduction-op div))
(def negate subtract)
(defn exp-sum [& args] (apply + (mapv #(Math/exp %) args)))
(def sumexp (reduction-op exp-sum))
(def softmax (reduction-op #(/ (Math/exp (first %&)) (apply exp-sum %&))))


(def _name (field :name))
(def _value (field :value))
(def _children (field :children))

(def evaluate (method :evaluate))
(def toString (method :toString))

(def ConstantPrototype
  {:value 0
   :evaluate (fn [this _vars] (_value this))
   :toString (comp str _value)
   })
(defn ConstantConstructor [this value]
  (assoc this
    :value value))
(def Constant (constructor ConstantConstructor ConstantPrototype))

(def VariablePrototype
  {:name ""
   :evaluate (fn [this vars] (vars (_name this)))
   :toString _name
   })
(defn VariableConstructor [this name]
  (assoc this
    :name name))
(def Variable (constructor VariableConstructor VariablePrototype))

(defn reductionNode [op]
  (let [Prototype
          {:children (vector )
           :evaluate (fn [this vars] (apply op (mapv #(evaluate % vars) (_children this))))
           :toString _name
           }
        Constructor (fn [this & children] (assoc this :children (vec children)))
        ]
  (constructor Constructor Prototype)))

(def Add (reductionNode +))
(def Subtract (reductionNode -))
(def Multiply (reductionNode *))
(def Divide (reductionNode div))
(def Negate Subtract)



(def functionalDictionary {:c constant, :v variable, '+ add, '- subtract, '* multiply,
                           '/ divide, 'negate negate, 'sumexp sumexp, 'softmax softmax})

(def objectDictionary {:c Constant, :v Variable, '+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate})

(defn parseTokenStream [dictionary token]
  (cond
    (number? token) ((dictionary :c) token)
    (symbol? token)
      ((dictionary :v)
       (name token))
    (list? token) (apply (dictionary (first token)) (mapv (partial parseTokenStream dictionary) (drop 1 token)))))

(def parseFunction (comp (partial parseTokenStream functionalDictionary) read-string))
(def parseObject (comp (partial parseTokenStream objectDictionary) read-string))

(def expr
  (Subtract
    (Multiply
      (Constant 2)
      (Variable "x"))
    (Constant 3)))


(defn -main []
  (println (evaluate (parseObject "(- (* 2 x) 3)") {"x" 2}))
  (println (evaluate (Add (Constant 228) (Variable "x")) {"x" 3}))
  (println (evaluate expr {"x" 2}))
  (println (Constant 228))
  (println (_value (Constant 228)))
  (println (type (toString (Constant 228))))
  (println (/ 5.0 0))
  (println (div 5.0 0))
  (println (div 5 0))
  (println "====================")
  (println (functionalDictionary (eval '-)))
  (println {:x 1} {"x" 1})
  (println ({"x" 1} "x"))
  (println ({"x" 1} "y"))
  (println (= {:x 1} {"x" 1}))
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
