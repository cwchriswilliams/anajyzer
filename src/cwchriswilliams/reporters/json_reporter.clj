(ns cwchriswilliams.reporters.json-reporter
  (:require [cheshire.core :as json]))

(defn prettify-analysis
  [anajyze-map]
  (-> anajyze-map
      (dissoc :raw-text)
      (dissoc :file)
      (dissoc :forms)))

(defn generate-report
  [analysis-in]
  (-> (map prettify-analysis analysis-in)
      (json/generate-string {:pretty true})))

(defn report
  [config analysis-in]
  (let [output-path (:output-path config)
        json-out (generate-report analysis-in)]
    (println "Output the results to: " output-path)
    (spit output-path json-out)))