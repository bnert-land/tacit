(ns tacit.core)

(set! *warn-on-reflection* true)
(assert (= "21" (System/getProperty "java.specification.version"))
        "Library only usable with Java 21 or greater")

(def executor*
  (java.util.concurrent.Executors/newVirtualThreadPerTaskExecutor))

; Took inspiration from `(future)` macro/call stack
; other method wasn't actually working/executing asynchronously
(defn virtual-call [f]
  (.submit ^java.util.concurrent.ExecutorService executor*
           ^java.util.concurrent.Callable f))

(defmacro virtual-let [bindings & body]
  `(virtual-call (fn* [] (let ~bindings ~@body))))

(defmacro virtual [& body]
  `(virtual-let [] ~@body))

