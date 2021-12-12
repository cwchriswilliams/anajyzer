(ns cwchriswilliams.analyzers.reference-count-analyzer-test
  (:require [clojure.test :refer [testing is deftest]]
            [cwchriswilliams.analyzers.reference-count-analyzer :as sut]))

(deftest count-references-test
  (testing "Returns 0 for empty form collection"
    (is (= {:reference-count 0} (sut/count-references []))))
  (testing "Returns 0 for form collection containing no ns macros"
    (is (= {:reference-count 0} (sut/count-references ['{} '[] '(println "Bob")]))))
  (testing "Returns 0 for form collection containing ns macros with no requries"
    (is (= {:reference-count 0} (sut/count-references ['(ns) '(ns)]))))
  (testing "Returns 0 for form collection containing require keywords outside ns"
    (is (= {:reference-count 0} (sut/count-references ['(:require) '(:require [reference])]))))
  (testing "Returns the number of requires in a single ns macro"
    (is (= {:reference-count 1} (sut/count-references ['(ns (:require [reference]))])))
    (is (= {:reference-count 2} (sut/count-references ['(ns (:require [reference] [reference2]))])))
    (is (= {:reference-count 3} (sut/count-references ['(ns (:require [reference] [reference2] [reference3]))]))))
  (testing "Returns the number of requires in multiple ns macros"
    (is (= {:reference-count 2} (sut/count-references ['(ns (:require [reference])) '(ns (:require [reference2]))])))
    (is (= {:reference-count 3} (sut/count-references ['(ns (:require [reference])) '(ns (:require [reference2])) '(ns (:require [reference3]))])))
    (is (= {:reference-count 3} (sut/count-references ['(ns (:require [reference])) '(ns (:require [reference2] [reference3]))])))))