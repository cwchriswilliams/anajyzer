(ns cwchriswilliams.reporters.stdout-reporter 
  (:require [clojure.string :as string]))

(defn prettify-analysis
  [anajyze-map]
  (-> anajyze-map
      (dissoc :raw-text)
      (dissoc :file)
      (dissoc :forms)))

(defn generate-report
  [anajyze-maps]
  (string/join "\n" (sort-by :path (map prettify-analysis anajyze-maps))))

(defn report
  [_config analysis-in]
  (println (generate-report analysis-in)))