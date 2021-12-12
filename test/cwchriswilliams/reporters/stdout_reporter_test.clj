(ns cwchriswilliams.reporters.stdout-reporter-test
  (:require [clojure.test :refer [testing is deftest]]
            [cwchriswilliams.reporters.stdout-reporter :as sut]))

(deftest generate-report-test
  (testing "Returns empty string for empty input"
    (is (= "" (sut/generate-report []))))
  (testing "Returns empty map for each empty input"
    (is (= "{}" (sut/generate-report [{}])))
    (is (= "{}\n{}" (sut/generate-report [{} {}]))))
  (testing "Removes fields that do not serialize well or are too long to be useful"
    (is (= "{}" (sut/generate-report [{:raw-text "Some-text"}])))
    (is (= "{}" (sut/generate-report [{:file "file"}])))
    (is (= "{}" (sut/generate-report [{:forms "forms"}])))
    (is (= "{}" (sut/generate-report [{:raw-text "Some-text" :file "file" :forms "forms"}]))))
  (testing "Returns all other fields as json"
    (is (= "{:analyzer-1 \"Result1\"}" (sut/generate-report [{:raw-text "Some-text" :analyzer-1 "Result1"}])))
    (is (= "{:analyzer-2 \"Result2\"}" (sut/generate-report [{:raw-text "Some-text" :analyzer-2 "Result2"}])))))