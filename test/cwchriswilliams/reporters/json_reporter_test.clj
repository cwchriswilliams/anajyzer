(ns cwchriswilliams.reporters.json-reporter-test
  (:require [clojure.test :refer [testing is deftest]]
            [cwchriswilliams.reporters.json-reporter :as sut]))

(deftest generate-report-test
  (testing "Returns empty array for empty input"
    (is (= "[ ]" (sut/generate-report []))))
  (testing "Returns empty map for each empty input"
    (is (= "[ { } ]" (sut/generate-report [{}])))
    (is (= "[ { }, { } ]" (sut/generate-report [{} {}]))))
  (testing "Removes fields that do not serialize well or are too long to be useful"
    (is (= "[ { } ]" (sut/generate-report [{:raw-text "Some-text"}])))
    (is (= "[ { } ]" (sut/generate-report [{:file "file"}])))
    (is (= "[ { } ]" (sut/generate-report [{:forms "forms"}])))
    (is (= "[ { } ]" (sut/generate-report [{:raw-text "Some-text" :file "file" :forms "forms"}]))))
  (testing "Returns all other fields as json"
    (is (= "[ {\n  \"analyzer-1\" : \"Result1\"\n} ]" (sut/generate-report [{:raw-text "Some-text" :analyzer-1 "Result1"}])))
    (is (= "[ {\n  \"analyzer-2\" : \"Result2\"\n} ]" (sut/generate-report [{:raw-text "Some-text" :analyzer-2 "Result2"}])))))