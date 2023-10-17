(ns user)

(comment
  (require '[tacit.core :as t] :reload)
  (require '[clojure.edn :as edn])

  (deref
    (t/virtual
      (println "hi")
      100))

  (deref
    (t/virtual-let [value "{:msg \"hello\"}"]
      (Thread/sleep 1000)
      (edn/read-string value)))


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
