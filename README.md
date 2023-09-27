# tacit

Java 21 Virtual Thread Utilities

## SMTC (Show Me The Code)

```clojure
(ns user)

(require
  '[clojure.edn :as edn]
  '[clojure.java.io :as io]
  '[tacit.core :refer [virtual virtual-let]])

; Will execute operation on a virtual thread.
; Result of the function is available via deref, given the underlying
; threading model returns a Future<T>.
(-> @(virtual (slurp (io/file "some-file.edn"))) ; will "park" on io wait
    edn/read-string)

(-> @(virtual-let [fname "config.edn"] (io/resource fname))
    slurp
    edn/read-string))
```

## Motivation

Java 21's Virtual Threads have some usage "semantics" (for lack of a better word)
which would make usage in Clojure programs kind of "meh".

These utilities aim to cover some basic use cases.

## API

TODO
