(ns virtual.async
  (:import
    java.util.concurrent.Executors))

(set! *warn-on-reflection* true)
(assert (= "21" (System/getProperty "java.specification.version"))
        "Library only usable with Java 21 or greater")


(def async?
  (try
    (require 'clojure.core.async)
    true
    (catch Exception _e
      false)))

(when async?
  (require '[clojure.core.async.impl.dispatch]
           '[clojure.core.async.impl.protocols]))

; Not sure if this is a good idea or not, buuuuut
; we'll see how it plays out in practice.
;
; Fundamentally, given that the clojure.core.async yeilds threads
; based on it's runtime, this should be a drop in replacement, essentially.
;
; From basic async tests, this is working as expected.
(defn use-virtual-thread-executor!
  "Patches the thread executor use by core.async to use
  Java 21 virtual threads"
  []
  (when async?
    ; Not sure if this should be close over or be invoked on
    ; each call to impl.protocols/exec...
    (let [e (Executors/newVirtualThreadPerTaskExecutor)]
      (alter-var-root
        #_:clj-kondo/ignore
        #'clojure.core.async.impl.dispatch/executor
        (fn [_]
          (delay
            (reify
              #_:clj-kondo/ignore
              clojure.core.async.impl.protocols/Executor
              (clojure.core.async.impl.protocols/exec [_this runnable]
                (.submit ^java.util.concurrent.ExecutorService e 
                         ^Runnable runnable)))))))))

