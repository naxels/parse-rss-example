(ns parse-rss-example.core
  (:gen-class)
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]))

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

;; main function
(defn -main
  [& uri-arg] ; need & in case no arg
  (if uri-arg
    (let [rss-titles (parse-uri-and-grab-titles (first uri-arg))]
      (run! println rss-titles)) ; quick way to do function on coll with side effects
    (println "Add RSS URL as argument"))
  )
