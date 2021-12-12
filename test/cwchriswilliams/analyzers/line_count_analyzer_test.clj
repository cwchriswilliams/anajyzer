(ns cwchriswilliams.analyzers.line-count-analyzer-test
  (:require [clojure.test :refer [testing is deftest]]
            [cwchriswilliams.analyzers.line-count-analyzer :as sut]))

(deftest line-count-test
  (testing "Returns 0 for empty string"
    (is (= {:line-count 0} (sut/line-count "")))
    (is (= {:line-count 0} (sut/line-count "\r\t "))))
  (testing "Ignores whitespace only lines"
    (is (= {:line-count 0} (sut/line-count "\n")))
    (is (= {:line-count 0} (sut/line-count "\n\n")))
    (is (= {:line-count 0} (sut/line-count "\n           \n \t\r\n"))))
  (testing "Returns number of non-blank lines"
    (is (= {:line-count 1} (sut/line-count "Some Text")))
    (is (= {:line-count 1} (sut/line-count "\n\r\tSome Text\n")))
    (is (= {:line-count 2} (sut/line-count "Some\nText")))
    (is (= {:line-count 2} (sut/line-count "Some\nText\n")))
    (is (= {:line-count 3} (sut/line-count "Some\nText\nHere")))))


