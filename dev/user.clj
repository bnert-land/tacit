(ns user)

(comment
  (require '[virtual.core :as v] :reload)
  (require '[clojure.edn :as edn])

  (deref
    (v/virtual
      (println "hi")
      100))

  (deref
    (v/virtual-let [value "{:msg \"hello\"}"]
      (Thread/sleep 1000)
      (edn/read-string value)))


  (require 'virtual.async :reload)
  (virtual.async/use-virtual-thread-executor!)

  (require '[clojure.core.async
             :refer [chan put! go-loop <!]])

  (def c (chan))

  (do
    (dotimes [i 1000]
      (put! c {:value i}))
    ; signal to stop loop
    (put! c {}))

  (go-loop []
    (let [v? (<! c)
          t  (Thread/currentThread)]
      ; Should print: "VirtualThreads> {...}"
      (println (str (.getName (.getThreadGroup t)) ">") v?)
      (when (:value v?)
      (recur))))
)
