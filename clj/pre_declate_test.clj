(ns pre_declate_test)

(load-file "proto.clj")

(def _name (field :name))
(def _value (field :value))
(def _children (field :children))

(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))


;(declare Constant)
(def ConstantPrototype
  {:value 0
   :evaluate (fn [this _vars] (_value this))
   :toString (comp str _value)
   :diff nil
   })
(defn ConstantConstructor [this value]
  (assoc this
    :value value))
(def Constant (constructor ConstantConstructor ConstantPrototype))

(defn -main
  (println (Constant 1)))
