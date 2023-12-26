(ns virtual.async-test
  (:require
    [clojure.core.async :refer [go <!!]]
    [clojure.test :refer [deftest is testing]]
    [virtual.async :refer [use-virtual-thread-executor!]]))

(use-virtual-thread-executor!)

(deftest using-virtual
  (testing "using virtual threads for async thread executor"
    (is (= "VirtualThreads"
           (<!! (go (.getName (.getThreadGroup (Thread/currentThread)))))))))

