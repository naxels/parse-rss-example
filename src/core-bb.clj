#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.data.xml :as xml])

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
  ; (get-value (first (filter #(= :title (:tag %)) (:content entry)))))
  ;; (get-value (first (filter title-tag? (:content entry)))))
  (get-value (some #(when (title-tag? %) %) (:content entry))))

(defn to-xml
  [uri]
  (-> uri
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
  (->> (io/reader uri)
       (to-xml)
       (get-items)
       (get-titles)))

(if *command-line-args*
  (let [rss-titles (parse-uri-and-grab-titles (first *command-line-args*))]
    (run! println rss-titles))
  (println "Add RSS URL as argument"))
