(ns virtual.core)

(set! *warn-on-reflection* true)

(def executor*
  (java.util.concurrent.Executors/newVirtualThreadPerTaskExecutor))

(defn virtual-call
  "Takes a function and executes it on the module level virtual thread pool
  executor. Returns java.util.concurrent.Future, which can be dereferenced
  in order to get result value"
  {:static true}
  [f]
  (.submit ^java.util.concurrent.ExecutorService executor*
           ^java.util.concurrent.Callable f))

; Took inspiration from `(future)` macro/call stack
; other method wasn't actually working/executing asynchronously
(defmacro virtual-let
  ^{:clj-kondo/lint-as 'clojure.core/let}
  [bindings & body]
  `(virtual.core/virtual-call
     (bound-fn*
       (^{:once true} fn* [] (let ~bindings ~@body)))))

(defmacro virtual
  ^{:clj-kondo/lint-as 'def-catch-all}
  [& body]
  `(virtual.core/virtual-let [] ~@body))

