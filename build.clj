(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'parse-rss-example/parse-rss-example)
(def version (format "0.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uber [_]
  (clean nil)
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src"] ; ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :main 'parse-rss-example.core
           :uber-file uber-file
           :basis basis}))
