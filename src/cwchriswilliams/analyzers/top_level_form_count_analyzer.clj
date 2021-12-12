(ns cwchriswilliams.analyzers.top-level-form-count-analyzer
  (:require [cwchriswilliams.analyzers.analyzers :as analy]))

(defn count-top-level-forms
  [forms]
  {:top-level-form-count (count forms)})

(def top-level-forms-count-analyzer (analy/build-forms-analyzer-definition count-top-level-forms))

