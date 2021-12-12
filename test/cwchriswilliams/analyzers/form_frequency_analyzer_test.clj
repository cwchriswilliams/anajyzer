(ns cwchriswilliams.analyzers.form-frequency-analyzer-test
  (:require [clojure.test :refer [testing is deftest]]
            [cwchriswilliams.analyzers.form-frequency-analyzer :as sut]))


(deftest caclulate-form-frequency-test
  (testing "Returns empty map for empty form collection"
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency []))))
  (testing "Returns form frequency for all forms"
    (is (= {:form-frequency {'println 1}} (sut/caclulate-form-frequency ['(println "bob")])))
    (is (= {:form-frequency {'println 2}} (sut/caclulate-form-frequency ['(println "bob") '(println "fred")])))
    (is (= {:form-frequency {'println 2 'fun 1}} (sut/caclulate-form-frequency ['(println "bob") '(println "fred") '(fun operand)]))))
  (testing "Includes inner forms"
    (is (= {:form-frequency {'println 2}} (sut/caclulate-form-frequency ['(println (println 2))])))
    (is (= {:form-frequency {'println 1 'fun 1}} (sut/caclulate-form-frequency ['(println (fun 2))]))))
  (testing "Ignores non-calls"
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency ['{}])))
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency ['{:map-key value}])))
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency ['[:vec-elem-1 vec-elem-2]])))
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency ['#{1 2 3}])))
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency ['4])))
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency ['+])))
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency ["string"])))
    (is (= {:form-frequency {}} (sut/caclulate-form-frequency ['("string")])))))