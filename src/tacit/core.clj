(ns tacit.core)

(set! *warn-on-reflection* true)
(assert (= "21" (System/getProperty "java.specification.version"))
        "Library only usable with Java 21 or greater")

(defmacro virtual-let [bindings & body]
  `(with-open [e# (java.util.concurrent.Executors/newVirtualThreadPerTaskExecutor)]
    (.submit ^java.util.concurrent.ExecutorService e#
             ^java.util.concurrent.Callable
             (fn [] 
               (let ~bindings
                 ~@body)))))

(defmacro virtual [& body]
  `(virtual-let [] ~@body))

