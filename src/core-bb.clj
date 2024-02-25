#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.data.xml :as xml])

(defn to-xml
  [uri]
  (-> uri
      (xml/parse)
      (xml-seq)))

(defn get-item-titles
  [seqd-xml]
  (for [x seqd-xml
        :let [xc (:content x)
              content (comp first :content first)] ; fn
        :when (= :item (:tag x))]
    (content (filter (comp #{:title} :tag) xc))))

(defn parse-uri-and-grab-titles
  [uri]
  (->> (io/reader uri)
       (to-xml)
       (get-item-titles)))

(if *command-line-args*
  (let [rss-titles (parse-uri-and-grab-titles (first *command-line-args*))]
    (run! println rss-titles))
  (println "Add RSS URL as argument"))
