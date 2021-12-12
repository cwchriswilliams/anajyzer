(ns cwchriswilliams.config
  (:require [clojure.java.io :as io]
            [aero.core :as aero]))

(defn read-default-config
  []
  (aero/read-config (io/resource "default_config.edn")))

(defmethod aero/reader 'anajyzer/v1 [_opts _tag value] value)

(defn load-config
  [path]
  (let [file (io/file path)]
    (aero/read-config file)))

(defn load-and-merge-configs
  ([path]
   (let [def-config (read-default-config)
         new-config (load-config path)]
     (merge def-config new-config)))
  ([]
   (read-default-config)))