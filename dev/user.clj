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
)
