;(ns expression)

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


(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))
(def functionSymbol (method :functionSymbol))

(def _children (field :children))
(def _immediate-children (field :immediate-children))
(def _inner (field :inner))

(declare Constant)
(declare Variable)
(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Negate)

(defclass Constant _ [value]
          (evaluate [vars] (_value this))
          (diff [var] (Constant 0))
          (toString [] (str (_value this))))

(defclass Variable _ [name]
          (evaluate [vars] (vars (_name this)))
          (diff [var] (Constant (if (= var (_name this)) 1 0)))
          (toString [] (_name this)))


(defn reductionNode [op, name, differentiation]
  (let [Prototype
          {:children (vector )
           :evaluate (fn [this vars] (apply op (mapv #(evaluate % vars) (_children this))))
           :functionSymbol (fn[this] name)
           :toString (fn [this]
                       (str "(" name " " (clojure.string/join " " (mapv toString (_children this))) ")"))
           :diff differentiation
           }
        Constructor (fn [this & children] (assoc this :children (vec children)))
        ]
    (constructor Constructor Prototype)))

(defn recursive-diff [Container]
  (fn [this var] (do
                   (println this)
                   (apply Container (mapv #(diff % var) (_children this)))
                   )))

(defn diff-term [ch index var]
  (apply Multiply
         (concat
           [(diff (nth ch index) var)]
           (take index ch)
           (take-last (- (count ch) index 1) ch))))

(def Add (reductionNode + "+" (fn [this var] (apply Add (mapv #(diff % var) (_children this))))))
(def Subtract (reductionNode - "-" (fn [this var] (apply Subtract (mapv #(diff % var) (_children this))))))
(def Multiply (reductionNode * "*" (fn [this var] (let [ch (_children this)]
  (apply Add (mapv (fn [index] (diff-term ch index var)) (range (count ch))))))))

; div(first, ...) = first / prod(...)
(def Divide (reductionNode div "/"
                           (fn [this var] (let [ch (_children this)]
                                            (if (= 1 (count ch))
                                              (diff (Divide (Constant 1) (first ch)) var)
                                              (Divide
                                                (apply Subtract
                                                       (diff-term ch 0 var)
                                                       (mapv #(diff-term ch % var) (range 1 (count ch))))
                                                (apply Multiply (mapv #(Multiply % %) (drop 1 ch)))))))))

(defn labeledTree [treeConstructor, label]
  (let [Prototype
        {:immediate-children (vector )
         :inner nil
         :evaluate (fn [this vars] (evaluate (_inner this) vars))
         :toString (fn [this] (str "(" label " " (clojure.string/join " " (mapv toString (_immediate-children this))) ")"))
         :diff (fn [this var] (diff (_inner this) var))
         }
        Constructor (fn [this & imm-children] (assoc this :immediate-children (vec imm-children) :inner (apply treeConstructor imm-children)))
        ]
    (constructor Constructor Prototype)))


(def Negate (labeledTree Subtract "negate"))



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

(def e2 (diff (Divide (Constant 5.0) (Variable "z")) "x"))
(def single-div (diff (Divide (Variable "x")) "x"))

(defn -main []
  (println e2)
  (println (toString single-div))
  (println (toString e2))
  (println (toString (Subtract (Constant 3.0) (Variable "y"))))
  (println (toString (Variable "x")))
  (println (evaluate (diff expr "x") {"x" 10}))
  (println (evaluate
             (diff (Add (Constant 228) (Variable "x")) "x") {"x" 3}))
  (println (evaluate (Constant 1) {"x" 2}))
  (println (diff (Constant 1) "x"))
  ;(println (evaluate (parseObject "(- (* 2 x) 3)") {"x" 2}))
  (println (let [e (diff (Constant 1) "x")]
             (evaluate e {"x" 2})))
  ;(println (evaluate expr {"x" 2}))
  ;(println (Constant 228))
  ;(println (_value (Constant 228)))
  ;(println (type (toString (Constant 228))))
  ;(println (/ 5.0 0))
  ;(println (div 5.0 0))
  ;(println (div 5 0))
  ;(println "====================")
  ;(println (functionalDictionary (eval '-)))
  ;(println {:x 1} {"x" 1})
  ;(println ({"x" 1} "x"))
  ;(println ({"x" 1} "y"))
  ;(println (= {:x 1} {"x" 1}))
  ;(println (eval '(+ 1 2)))
  ;(println (eval (read-string "(+ 1 2)")))
  ;(println (read-string "(- (* 2 x) 3)"))
  ;(println (read-string "x") (type (read-string "x")) (name (read-string "x")))
  ;(println (add (variable "x") (constant 2.0)))
  ;(println (parseFunction "(+ x 2.0)"))
  ;(println (parseFunction "(/ (negate x) 2.0)"))
  ;(println (type (name 'softmax)))
  ;(println (#(Math/exp %) 0))
  ;(println ((sumexp (variable "x")) {"z" 0.0, "x" 0.0, "y" 0.0}))
  ;(println (((eval 'softmax) (constant 1) (constant 2) (constant 3)) {}))
  )
