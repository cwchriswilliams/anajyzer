(ns cwchriswilliams.anajyzer
  (:gen-class)
  (:require [clojure.string :as string]
            [cwchriswilliams.analyzers.analyzers :as analy]
            [cwchriswilliams.config :as config]
            [cwchriswilliams.file-management :as fmgmt]))

(def file-fns
  {:get-file-name #(.getPath ^java.io.File %)
   :read-file-contents slurp})

(defn ->anajyze-map
  [file-fns in-file raw-text]
  {:file in-file :raw-text raw-text :file-path ((:get-file-name file-fns) in-file)})

(defn read-forms
  [raw-text]
  (read-string (str "[" raw-text "]")))

(defn assoc-forms-analyzers
  [analyzers anajyze-map]
  (let [forms (:forms anajyze-map)]
    (merge anajyze-map (analy/perform-form-analysis analyzers forms))))

(defn assoc-raw-text-analyzers
  [analyzers anajyze-map]
  (let [raw-text (:raw-text anajyze-map)]
    (merge anajyze-map (analy/perform-raw-text-analysis analyzers raw-text))))

(defn assoc-form-data
  [anajyze-map]
  (let [raw-text (:raw-text anajyze-map)]
    (assoc anajyze-map :forms (read-forms raw-text))))

(defn analyze-data
  [analyzers anajyze-map-with-raw-text]
  (->> anajyze-map-with-raw-text
       (assoc-raw-text-analyzers analyzers)
       (assoc-form-data)
       (assoc-forms-analyzers analyzers)))

(defn anajyze-file
  [file-fns analyzers in-file]
  (->> in-file
       ((:read-file-contents file-fns))
       (->anajyze-map file-fns in-file)
       (analyze-data analyzers)))

(defn anajyze-files
  [file-fns analyzers files]
  (map (partial anajyze-file file-fns analyzers) files))

(defn anajyze-paths
  [file-fns analyzers paths]
  (let [files-to-proc (fmgmt/validate-code-paths paths)
        invalid-files-to-proc (remove :is-valid? files-to-proc)
        invalid-file-paths (map :path invalid-files-to-proc)]
    (if (seq invalid-file-paths)
      (println "The following code-paths could not be accessed: " (string/join "\n" invalid-file-paths))
      (-> files-to-proc
          ((partial map :file))
          fmgmt/get-source-files
          fmgmt/filter-for-clj-files
          ((partial anajyze-files file-fns analyzers))))))

(defn report-results
  [config analysis reporters]
  (doall (map #(% config analysis) reporters)))

(defn resolve-symbol
  [in-symbol]
  (if-let [in-symbol-ns (namespace in-symbol)]
    (require (symbol in-symbol-ns))
    (throw (ex-info "Failed to find namespace for symbol" {:in-symbol in-symbol})))
  (if-let [resolved (resolve in-symbol)]
    resolved
    (throw (ex-info (str "Failed to resolve symbol " in-symbol) {:in-symbol in-symbol}))))

(defn resolve-symbols
  [reporters]
  (map resolve-symbol reporters))

(defn resolve-analyzers
  [config]
  (-> config
      :analyzers
      resolve-symbols
      ((partial map var-get))))

(defn resolve-reporters
  [config]
  (-> config
      :reporters
      resolve-symbols))

(defn anajyze-main
  [& args]
  (let [config (config/load-and-merge-configs)
        reporters (resolve-reporters config)
        analyzers (resolve-analyzers config)
        results (anajyze-paths file-fns analyzers (:src-paths config))]
    (report-results config results reporters)))
