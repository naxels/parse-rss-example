(ns parse-rss-example.core
  (:gen-class)
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]))

(defn to-xml
  "Parse the xml & turn into an xml-seq which uses tree-seq to unfold all the nodes of the RSS feed"
  [uri]
  (-> uri
      (xml/parse)
      (xml-seq)))

(defn get-item-titles
  "Filter all the parsed XML nodes on :item and then :title
   Return the titles"
  [seqd-xml]
  (for [x seqd-xml
        :let [xc (:content x)
              content (comp first :content first)] ; fn
        :when (= :item (:tag x))]
    (content (filter (comp #{:title} :tag) xc))))

(defn parse-uri-and-grab-titles
  "Main worker to get all titles from RSS Items after parsing XML"
  [uri]
  (->> (io/reader uri)
       (to-xml)
       (get-item-titles)))

;; main function
(defn -main
  [& uri-arg] ; need & in case no arg
  (if uri-arg
    (let [rss-titles (parse-uri-and-grab-titles (first uri-arg))]
      (run! println rss-titles)) ; quick way to do function on coll with side effects
    (println "Add RSS URL as argument"))
  )
