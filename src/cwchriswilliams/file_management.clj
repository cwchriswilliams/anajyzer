(ns cwchriswilliams.file-management
  (:require [clojure.java.io :as io]))

(defn validate-code-path
  [path]
  (let [path-to-file (io/file path)
        is-valid? (and (.exists path-to-file)
                       (.isDirectory path-to-file))]
    {:is-valid? is-valid? :file path-to-file :path path}))

(defn validate-code-paths
  [paths]
  (map validate-code-path paths))

(defn get-source-files
  [directory-files]
  (mapcat (comp file-seq :file) (validate-code-paths directory-files)))

(defn is-clj-file?
  [file]
  (and (not (.isDirectory file))
       (filter #{"clj", "cljs"} (.getName file))))

(defn filter-for-clj-files
  [files]
  (filter is-clj-file? files))

