(ns user)

(comment
  (require '[tacit.core :as t] :reload)
  (require '[clojure.edn :as edn])

  (-> 
    (t/virtual
      (println "hi")
      100)
    deref)

  (->
    (t/virtual-let [value "{:msg \"hello\"}"]
      (edn/read-string value))
    deref)


  (require 'tacit.async :reload)
  (tacit.async/use-virtual-thread-executor!)

  (require '[clojure.core.async :refer [chan put! poll! go go-loop >! <!]])

  (def c (chan))

  (dotimes [i 1000]
    (put! c {:value i}))

  (go-loop []
    (let [v? (<! c)]
      (println "V?>" v?)
      (recur)))
)
