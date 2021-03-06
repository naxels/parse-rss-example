(ns core
  (:gen-class)
  (:require [clojure.xml :as xml]
            ; [clojure.data.xml]
            ; [clojure.java.io :as io]
            ; [clojure.inspector]
            ; [clojure.pprint]
            ))

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
  (get-value (first (filter title-tag? (:content entry)))))

;; declare feed
; (def uri "https://cointelegraph.com/rss")
; (def uri (first *command-line-args*))

;; slurp feed as string
; (def feed-str (slurp uri))

;; turn string to xml
; (def feed (clojure.xml/parse feed-str))
; (def feed (xml-seq (xml/parse uri)))

; (clojure.inspector/inspect feed)

; (map :tag feed)

;; filter items
; (def rss-entries (filter #(= :item (:tag %)) feed))
; (def rss-entries (filter item-tag? feed))

;; directly filter all titles from feed (issue: this will include feed title and other potential title's)
; (def titles_tags (filter #(= :title (:tag %)) feed))

;; (def titles (map #(first (:content %)) titles_tags))

;; (clojure.inspector/inspect rss-entries)

;; (map :content rss-entries)

;; filter title on first entry
;; (filter #(= :title (:tag %)) (:content (first rss-entries)))

;; inspect 1 entry
;; (clojure.inspector/inspect (first (:content (first (filter title-tag? (:content (first rss-entries)))))))
;; you can uncomment the inspect function on any position to view what is happening with the data
; (->> rss-entries
;      (clojure.inspector/inspect)
;      (first)
;      (clojure.inspector/inspect)
;      (:content)
;      (clojure.inspector/inspect)
;      (filter title-tag?)
;      (clojure.inspector/inspect)
;      (first)
;      (clojure.inspector/inspect)
;      (:content)
;      (clojure.inspector/inspect)
;      (first)
;      (clojure.inspector/inspect))

;; from the above we see that we need to map over each entry and execute the transformations
; (def rss-titles (map #(rss-title %) rss-entries))

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

  ;; (clojure.pprint/pprint rss-titles) ; pretty prints the list
  ;; (doseq [title rss-titles] ; for more complicated side effect expressions on coll
    ;; (println title))
  )

;; testing since comment won't get evaluated
(comment
  (-main "https://cointelegraph.com/rss"))