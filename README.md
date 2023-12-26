# virtual

[![Clojars Project](https://img.shields.io/clojars/v/land.bnert/virtual.svg)](https://clojars.org/land.bnert/virtual)

Java 21 Virtual Thread Utilities

## Motivation

Provide a more "clojure-y" syntax over Java 21's Virtual Thread Executor.


## Usage

With "future-like" api:
```clojure
(ns example.core
  (:require
    [virtual.core :refer [virtual virtual-let]]))

(defn -main [& _args]
    ; Will execute operation on a virtual thread.
    ; Result of the function is available via deref, given the underlying
    ; threading model returns a Future<T>.
    (-> @(virtual (slurp (io/file "some-file.edn"))) ; should "park" on io
        edn/read-string)

    (-> @(virtual-let [fname "config.edn"] (io/resource fname))
        slurp
        edn/read-string))
```

Monkey patching `clojure.core.async` to use virtual threads as the
thread executor:
```clojure
(ns example.core
  (:require
    [clojure.core.async :refer [go chan <!! <! >! take]]
    [virtual.async :refef [use-virtual-thread-executor!]]))

; Should be called, given this has a side effect of modifying the clojure.core.async
; thread pool executor.
(use-virtual-thread-executor!)

(def messages (chan))
(def processed (chan))

(def message-processor
  (go-loop []
    (let [value      (<! messages)
          group-name (.getName (.getThreadGroup (Thread/currentThread)))]
      (>! processed (into value {:processor-thread-group grou-name})))
    (recur)))

(defn -main [& _args]
  (dotimes [i 10]
    (put! messages {:from i :to (dec i), :message "hello!"}))
  (println (<!! (take 10 processed))))
```

