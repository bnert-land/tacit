(ns build
  (:require
    [clojure.tools.build.api :as b]
    [publicize.core :as p]))

; 
(def lib 'land.bnert/virtual)
(def version "0.2.0")

; -- build stuff
(def basis
  (p/clean-clojure-dep
    (b/create-basis {:project "deps.edn"})))
(def class-dir "target/classes")
(def target-dir "target")
(def jar-path (format (str target-dir "/%s-%s.jar") (name lib) version))
(def pom-path (b/pom-path {:lib lib :class-dir class-dir}))
(def pom-data
  [[:licenses
    [:license
     [:name "Eclipse Public License version 2.0"]
     [:url  "https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt"]
     [:distribution "repo"]]]])

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (clean nil)
  (println "Writing pom...")
  (b/write-pom
    {:basis     basis
     :class-dir class-dir
     :lib       lib
     :pom-data  pom-data
     :src-dirs  (get basis :paths ["src"])
     :scm       {:url "https://github.com/bnert-land/virtual"}
     :version   version})
  (println "Done...")
  (println "Copying src...")
  (b/copy-dir
    {:src-dirs   (get basis :paths ["src"])
     :target-dir class-dir})
  (println "Building jar...")
  (b/jar
    {:class-dir class-dir
     :jar-file  jar-path})
  (println "Done."))

(defn publicize [_]
  (jar nil)
  (println "Publicizing...")
  (p/publicize
    {:jar-file jar-path
     :lib      lib
     :pom-file pom-path
     :version  version})
  (println "Done."))

