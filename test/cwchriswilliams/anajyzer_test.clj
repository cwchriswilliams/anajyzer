(ns cwchriswilliams.anajyzer-test
  (:require [clojure.test :refer [testing is deftest]]
            [cwchriswilliams.anajyzer :as sut]
            [cwchriswilliams.analyzers.analyzers :as analy]))


(def test-file-fns
  {:get-file-name (fn [in-file] (str in-file "-path"))
   :read-file-contents (fn [_] "(println \"bob\")")})

(deftest ->anajyze-map-test
  (testing "Constructs a map with file, raw-text and file-path elements set"
    (is (= {:file "file" :raw-text "raw-text" :file-path "file-path"} (sut/->anajyze-map test-file-fns "file" "raw-text")))
    (is (= {:file "file2" :raw-text "raw-text2" :file-path "file2-path"} (sut/->anajyze-map test-file-fns "file2" "raw-text2")))))

(deftest read-forms-test
  (testing "Reads empty collection for empty string"
    (is (= [] (sut/read-forms "")))
    (is (= [] (sut/read-forms "\r\n\t "))))
  (testing "Reads forms as a collection of forms"
    (is (= ['(println "bob")] (sut/read-forms "(println \"bob\")")))
    (is (= ['(println "bob") '(str "bob2")] (sut/read-forms "(println \"bob\") (str \"bob2\")")))
    (is (= ['(println "bob") '{:map-key "map-value"}] (sut/read-forms "(println \"bob\") {:map-key \"map-value\"}")))
    (is (= ['(println "bob") '[:vec-el-1 "vec-el-2" [8]]] (sut/read-forms "(println \"bob\") [:vec-el-1 \"vec-el-2\" [8] ]"))))
  (testing "Ignores whitespace between forms"
    (is (= ['(println "bob") '(str "bob2")] (sut/read-forms "(println \"bob\")\r\n\t(str \"bob2\")")))))

(deftest anajyze-file-test
  (let [test-call #(sut/anajyze-file test-file-fns % "input-file")
        no-analyzers-map {:file "input-file" :file-path "input-file-path" :forms '[(println "bob")] :raw-text "(println \"bob\")"}]
    (testing "Returns map with forms and raw-text set if no analyzers provided"
      (is (= no-analyzers-map (test-call []))))
    (testing "Returns the result of analysis with raw-text analyzers run"
      (is (= (merge no-analyzers-map {:raw-analyzer-1 "Result" :raw-analyzer-2 "Result2"})
             (test-call [(analy/build-raw-text-analyzer-definition (fn [_] (identity {:raw-analyzer-1 "Result"})))
                         (analy/build-raw-text-analyzer-definition (fn [_] (identity {:raw-analyzer-2 "Result2"})))]))))
    (testing "Returns the result of analysis with forms analyzers run"
      (is (= (merge no-analyzers-map {:forms-analyzer-1 "Result3" :forms-analyzer-2 "Result4"})
             (test-call [(analy/build-forms-analyzer-definition (fn [_] (identity {:forms-analyzer-1 "Result3"})))
                         (analy/build-forms-analyzer-definition (fn [_] (identity {:forms-analyzer-2 "Result4"})))]))))
    (testing "Returns the result of analysis with forms and raw-text analyzers run"
      (is (= (merge no-analyzers-map {:raw-analyzer-1 "Result" :raw-analyzer-2 "Result2" :forms-analyzer-1 "Result3" :forms-analyzer-2 "Result4"})
             (test-call [(analy/build-raw-text-analyzer-definition (fn [_] (identity {:raw-analyzer-1 "Result"})))
                         (analy/build-raw-text-analyzer-definition (fn [_] (identity {:raw-analyzer-2 "Result2"})))
                         (analy/build-forms-analyzer-definition (fn [_] (identity {:forms-analyzer-1 "Result3"})))
                         (analy/build-forms-analyzer-definition (fn [_] (identity {:forms-analyzer-2 "Result4"})))]))))))