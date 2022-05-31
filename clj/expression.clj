(ns expression)

(load-file "proto.clj")
(load-file "parser.clj")

(defn div ([arg] (/ 1 (double arg)))
  ([first & tail] (reduce (fn [l, r] (/ (double l) (double r))) (apply vector first tail))))

(defn foldr [f val coll]
  (if (empty? coll) val
                    (f (foldr f val (rest coll)) (first coll))))

(defn foldl [f val coll]
  (if (empty? coll) val
                    (foldl f (f val (first coll)) (rest coll))))

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
(def toStringInfix (method :toStringInfix))
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
(declare Exp)

(defclass Constant _ [value]
          (evaluate [vars] (_value this))
          (diff [var] (Constant 0))
          (toStringInfix [] (str (_value this)))
          (toString [] (str (_value this))))

(defclass Variable _ [name]
          (evaluate [vars] (vars (_name this)))
          (diff [var] (Constant (if (= var (_name this)) 1 0)))
          (toStringInfix [] (_name this))
          (toString [] (_name this)))


(defn reductionNode [op, name, differentiation]
  (let [Prototype
          {:children (vector )
           :evaluate (fn [this vars] (apply op (mapv #(evaluate % vars) (_children this))))
           :functionSymbol (fn[this] name)
           :toString (fn [this]
                       (str "(" name " " (clojure.string/join " " (mapv toString (_children this))) ")"))
           :toStringInfix (fn [this] (let [ch-str (map toStringInfix (_children this)) have-two (assert (= 2 (count ch-str)))]
                                  (str "(" (first ch-str) " " name " " (second ch-str) ")")))
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
(def Exp (reductionNode #(Math/exp %) "exp" (fn [this var] (Multiply this (diff (first (_children this)) var)))))

(defn labeledTree [treeConstructor, label]
  (let [Prototype
        {:immediate-children (vector )
         :inner nil
         :functionSymbol (fn [this] label)
         :evaluate (fn [this vars] (evaluate (_inner this) vars))
         :toString (fn [this] (str "(" label " " (clojure.string/join " " (mapv toString (_immediate-children this))) ")"))
         :toStringInfix (fn [this] (assert false))
         :diff (fn [this var] (diff (_inner this) var))
         }
        Constructor (fn [this & imm-children] (assoc this :immediate-children (vec imm-children) :inner (apply treeConstructor imm-children)))
        ]
    (constructor Constructor Prototype)))


(def Negate (labeledTree Subtract "negate"))

(def Sumexp (labeledTree (fn [& trees] (apply Add (mapv Exp trees))) "sumexp"))
(def Softmax (labeledTree (fn [& trees] (Divide (Exp (first trees)) (apply Sumexp trees))) "softmax"))


; =============== Simple parser ===============

(def functionalDictionary {:c constant, :v variable, '+ add, '- subtract, '* multiply,
                           '/ divide, 'negate negate, 'sumexp sumexp, 'softmax softmax})

(def objectDictionary {:c Constant, :v Variable, '+ Add, '- Subtract, '* Multiply, '/ Divide,
                       'negate Negate, 'sumexp Sumexp, 'softmax Softmax})

(defn parseTokenStream [dictionary token]
  (cond
    (number? token) ((dictionary :c) token)
    (symbol? token)
      ((dictionary :v)
       (name token))
    (list? token) (apply (dictionary (first token)) (mapv (partial parseTokenStream dictionary) (drop 1 token)))))

(def parseFunction (comp (partial parseTokenStream functionalDictionary) read-string))
(def parseObject (comp (partial parseTokenStream objectDictionary) read-string))

; =============== Combinator parser ===============

(defn -show [result]
  (if (-valid? result)
    (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
    "!"))
(defn tabulate [parser inputs]
  (do (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (-show (parser input)))) inputs)
      (println "--------------------------------")))

(defn +char-seq [s] (apply +seq (map #(+char (str %)) s)))
(defn +string [s] (+str (+char-seq s)))

(def *digit (+char "0123456789"))
(def *uint-str (+str (+plus *digit)))
(def *number (+map (comp Constant read-string)
                   (+seqf str
                          (+opt (+char "-"))
                          *uint-str
                          (+opt (+seqf str
                                       (+char ".")
                                       *uint-str))
                          )))

(def *space (+char " \t\n\r"))
(def *wss (+ignore (+star *space)))
(def *wsp (+ignore (+plus *space)))

(defn *operator [op] (+map (constantly op) (+string (functionSymbol (op 0)))))
(defn *operators [& ops] (apply +or (map *operator ops)))
(def *mul-div (*operators Multiply Divide))
(def *var (+map (comp Variable str) (+char "xyz")))

;Expr   -> Term ([ '+' | '-' ] Term)*
;
; Term  -> Factor ([ '*' | '/' ] Factor)*
;
;// For right associativity it would be:
;# Factor  ->
;#	| Atomic ** Factor
;#	| Atomic // Factor
;
; Factor  -> Atomic ([ '//' | '**' ] Atomic)*
;
; Atomic  ->
;	 | num
;  | var
;	 | '(' Expr ')'
;	 | '-' Factor
;	 | FuncName Factor
;
; FuncName -> 't0' | 'l0' | 'abs'

(defn *atomic [expr] (+or
                       *number
                       *var
                       (+seqn 1 (+char "(") expr (+char ")"))
                       ; - Factor?
                       ))

(defn *layer-cont [layer]
  (let [ops (layer :operators) deeper (layer :deeper)]
    (+seq *wss (apply *operators ops) *wss deeper *wss)))

(defn *layer-arr-parser "structured collection of this-layer operator tokens and parsed deeper layer" [layer]
  (let [ops (layer :operators) deeper (layer :deeper) p' (*layer-cont layer)]
    (+seq *wss deeper *wss (+star p'))))

(defn right-regroup "[x ([+ 10] [- 20])] → [[x +] [10 -] 20]" [[fst pairs]]
  (if (empty? pairs) (list pairs fst)
                     (let [op (first (first pairs))
                           [new-list last] (right-regroup [(second (first pairs)) (rest pairs)])]
                       [(cons [fst op] new-list) last])))

(defn *left-associativity-layer [layer]
  (let [construct (fn [parsed] (reduce (fn [acc [op tree]] (op acc tree)) (first parsed) (second parsed)))]
    (+map construct (*layer-arr-parser layer))))

(defn *right-associativity-layer [layer]
  (let [construct (fn [parsed] (foldr (fn [tree [a op]] (op a tree)) (second parsed) (first parsed)))]
    (+map (comp construct right-regroup) (*layer-arr-parser layer))))


(defn *layer-parser [layer] ((layer :associativity) layer))
;

;(def *infix-parser (let [term-layer {:deeper (*atomic *infix-parser), :operators [Add Subtract], :associativity *left-associativity-layer}
;                         term-layer-parser (*layer-parser term-layer)
;                         expr-layer {:deeper term-layer-parser, :operators [Add Subtract], :associativity :left *left-associativity-layer}
;                         ]
;                     ))

; ================================== Tests =============================================

(def expr
  (Subtract
    (Multiply
      (Constant 2)
      (Variable "x"))
    (Constant 3)))

(def e2 (diff (Divide (Constant 5.0) (Variable "z")) "x"))
(def single-div (diff (Divide (Variable "x")) "x"))
(def rrg (right-regroup ["x" (list ["+" "10"] ["-" "20"])]))
(def test-l (*left-associativity-layer {:deeper (*atomic *number), :operators [Add Subtract]}))
(def test-r (*right-associativity-layer {:deeper (*atomic *number), :operators [Add Subtract]}))

(defn -main []
  (println (foldl vector 1 [2 3 4]))
  (println (reduce vector 1 [2 3 4]))
  (println (foldr (fn [a b] (vector b a)) 4 [1 2 3]))
  (println (right-regroup ["x" (list )]))
  (println (foldr
             (fn [tree [a op]] (str "(" a op tree ")"))
             (second rrg)
             (first rrg)
             ))
  (tabulate *uint-str ["1" "1~" "12~" "123~" "" "~"])
  (tabulate *number ["1" "-1" "1.0" "-100.19"])
  (tabulate *wss ["" "~" "     ~" "\t~"])
  (tabulate *wsp ["" "~" "     ~" "\t~"])
  (tabulate *var ["x" "xyz"])
  (println (toStringInfix expr))
  ;(println (toStringInfix (Sumexp (Constant 1) (Constant 2) (Constant 3))))
  (tabulate (*atomic *number) ["x  2 " "20" "-2343" "(2000)"])
  (tabulate (+string "hello") ["hello" "hello123" "hell"])
  (tabulate *mul-div ["*" "/" "10"])
  (println (evaluate ((-value (*mul-div "*")) (Constant 10) (Constant 12)) {}))
  (tabulate (*layer-cont {:deeper (*atomic *number), :operators [Add Subtract]}) ["+10" "   +  x  "])
  (tabulate (*layer-arr-parser {:deeper (*atomic *number), :operators [Add Subtract]}) ["10" "x" "x+10" "  x   +  10  " "x+10-y" "+" "+ 10"])
  (println (toStringInfix (-value (test-l "1 + x + 100 - y"))))
  (println (toStringInfix (-value (test-r "1 + x + 100 - y"))))
  ;(println (toString single-div))
  ;(println (toString e2))
  ;(println (toString (Subtract (Constant 3.0) (Variable "y"))))
  ;(println (toString (Variable "x")))
  ;(println (evaluate (diff expr "x") {"x" 10}))
  ;(println (evaluate
  ;           (diff (Add (Constant 228) (Variable "x")) "x") {"x" 3}))
  ;(println (evaluate (Constant 1) {"x" 2}))
  ;(println (diff (Constant 1) "x"))
  ;;(println (evaluate (parseObject "(- (* 2 x) 3)") {"x" 2}))
  ;(println (let [e (diff (Constant 1) "x")]
  ;           (evaluate e {"x" 2})))
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
  ;(println [(#(Math/exp %) 0))
  ;(println ((sumexp (variable "x")) {"z" 0.0, "x" 0.0, "y" 0.0}))
  ;(println (((eval 'softmax) (constant 1) (constant 2) (constant 3)) {}))
  (println "")
  )
