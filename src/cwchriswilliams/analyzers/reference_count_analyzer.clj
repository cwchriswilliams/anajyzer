(ns cwchriswilliams.analyzers.reference-count-analyzer
  (:require [cwchriswilliams.analyzers.analyzers :as analy]))

(defn get-ns-forms
  [forms]
  (filter #(= 'ns (first %)) forms))

(defn get-require-from-ns-form
  [ns-form]
  (filter #(and (coll? %) (= :require (first %))) ns-form))

(defn get-require-forms-from-ns-forms
  [ns-forms]
  (mapcat get-require-from-ns-form ns-forms))

(defn get-reference-count-from-require-form
  [require-form]
  (count (drop 1 require-form)))

(defn get-reference-count-from-require-forms
  [require-forms]
  (reduce + (map get-reference-count-from-require-form require-forms)))


(defn count-references
  [forms]
  (-> forms
      get-ns-forms
      get-require-forms-from-ns-forms
      get-reference-count-from-require-forms
      (#(hash-map :reference-count %))))

(def reference-count-analyzer (analy/build-forms-analyzer-definition count-references))
