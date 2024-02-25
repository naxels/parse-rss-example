(ns parse-rss-example.core
  (:gen-class)
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]))

;; helper functions
(defn keyword-in-tag?
  [keyword entry]
  (= keyword (:tag entry)))

(def title-tag? (partial keyword-in-tag? :title))

(def item-tag? (partial keyword-in-tag? :item))

(defn get-value
  [node]
  (first (:content node)))

(defn rss-title
  [entry]
  (get-value (some #(when (title-tag? %) %) (:content entry))))

(defn to-xml
  [uri]
  (-> uri
      (io/input-stream)
      (xml/parse)
      (xml-seq)))

(defn get-items
  [xml-seqed]
  (filter item-tag? xml-seqed))

(defn get-titles
  [xml-items]
  (map #(rss-title %) xml-items))

(defn parse-uri-and-grab-titles
  [uri]
  (->> uri
       (to-xml)
       (get-items)
       (get-titles)))

;; main function
(defn -main
  [& uri-arg] ; need & in case no arg
  (if uri-arg
    (let [rss-titles (parse-uri-and-grab-titles (first uri-arg))]
      (run! println rss-titles)) ; quick way to do function on coll with side effects
    (println "Add RSS URL as argument"))
  )
