(ns cwchriswilliams.analyzers.form-frequency-analyzer
  (:require [cwchriswilliams.analyzers.analyzers :as analy]))

(defn forms-to-tree-seq
  [forms]
  (mapcat #(tree-seq coll? seq %) forms))

(defn filter-for-calls
  [forms]
  (filter #(and (list? %) (symbol? (first %))) forms))

(defn into-sorted-map-by-value-desc
  [m]
  (into (sorted-map-by (fn [key1 key2]
                             (compare
                              [(get m key2) key2]
                              [(get m key1) key1]))) m))


(defn calculate-frequencies [forms]
  (-> forms
      forms-to-tree-seq
      filter-for-calls
      ((partial map first))
      frequencies
      into-sorted-map-by-value-desc))


(defn caclulate-form-frequency
  [forms]
  {:form-frequency (calculate-frequencies forms)})

(def form-frequency-analyzer (analy/build-forms-analyzer-definition caclulate-form-frequency))
