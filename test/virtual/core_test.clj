(ns virtual.core-test
  (:require
    [virtual.core :refer [virtual]]
    [clojure.test :refer [deftest testing is]]))

(def ^:dynamic *meaning-of-life* 32)

(deftest binding-conveyance
  (testing "Binding conveyance"
    (is (= 32 @(virtual *meaning-of-life*)))
    (binding [*meaning-of-life* 42]
      (is (= 42 @(virtual *meaning-of-life*))))
    (is (= 52 @(virtual
                (binding [*meaning-of-life* 52]
                  *meaning-of-life*))))))
