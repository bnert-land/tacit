(ns virtual.core)

(set! *warn-on-reflection* true)
(assert (= "21" (System/getProperty "java.specification.version"))
        "Library only usable with Java 21 or greater")

(def executor*
  (java.util.concurrent.Executors/newVirtualThreadPerTaskExecutor))

#_:clj-kondo/ignore
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
(defmacro virtual-let [bindings & body]
  `(virtual.core/virtual-call
     (bound-fn*
       (^{:once true} fn* [] (let ~bindings ~@body)))))

(defmacro virtual [& body]
  `(virtual.core/virtual-let [] ~@body))

