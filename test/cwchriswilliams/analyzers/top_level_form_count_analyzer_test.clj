(ns cwchriswilliams.analyzers.top-level-form-count-analyzer-test
  (:require [clojure.test :refer [testing is deftest]]
            [cwchriswilliams.analyzers.top-level-form-count-analyzer :as sut]))

(deftest count-top-level-forms-test
  (testing "Returns 0 for empty form collection"
    (is (= {:top-level-form-count 0} (sut/count-top-level-forms []))))
  (testing "Returns the correct number of top level forms"
    (is (= {:top-level-form-count 1} (sut/count-top-level-forms ['(println "bob")])))
    (is (= {:top-level-form-count 2} (sut/count-top-level-forms ['(println "bob") '{:thing "bob2"}]))))
  (testing "Returns correct number of top level forms ignoring inner forms"
    (is (= {:top-level-form-count 1} (sut/count-top-level-forms ['(println (get-inner (get-inner-inner)))])))
    (is (= {:top-level-form-count 1} (sut/count-top-level-forms ['(println (get-inner (get-inner-inner) get-inner (get-inner-inner)) (get-inner (get-inner-inner) get-inner (get-inner-inner)))])))
    (is (= {:top-level-form-count 2} (sut/count-top-level-forms ['(println (get-inner)) '(println (get-inner))])))))