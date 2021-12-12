(ns cwchriswilliams.analyzers.line-count-analyzer
  (:require [clojure.string :as string]
            [cwchriswilliams.analyzers.analyzers :as analy]))

(defn line-count
  [raw-text]
  (let [line-count (count (remove string/blank? (string/split raw-text #"\n")))]
    {:line-count line-count}))

(def line-count-analyzer (analy/build-raw-text-analyzer-definition line-count))
