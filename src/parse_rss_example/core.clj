(ns parse-rss-example.core
  (:gen-class)
  (:require ; [clojure.xml :as xml]
            [clojure.data.xml :as xml]
            [clojure.java.io :as io]
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
  ;; (get-value (first (filter title-tag? (:content entry))))) ; filter uses chunking
  (get-value (some #(when (title-tag? %) %) (:content entry))))

;; declare feed
; (def uri "https://tweakers.net/feeds/mixed.xml")
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

  ;; (clojure.pprint/pprint rss-titles) ; pretty prints the list
  ;; (doseq [title rss-titles] ; for more complicated side effect expressions on coll
    ;; (println title))
  )

;; testing since comment won't get evaluated
(comment
  (-main "https://tweakers.net/feeds/mixed.xml")

  (require '[clojure.inspector])
  ;; (require '[clojure.java.io])
  (require '[clojure.pprint])
  ;; (require '[clojure.data.xml])

  ; try stackoverflow
  ; read xml & parse
  ; (map :tag parsed-xml)
  ; (filter #(= :link (:tag %)) parsed-xml)
  ; (filter #(= :item (:tag %)) parsed-xml)
  (->> "https://tweakers.net/feeds/mixed.xml"
       (clojure.java.io/input-stream)
       #_(xml/parse)
       (clojure.data.xml/parse)
       (:content)
       (first)
       #_(count)
       (:content)
       (filter item-tag?)
       #_(clojure.pprint/pprint)
       #_(clojure.inspector/inspect-tree)
       #_(keys)
       (map :tag)
       #_(filter #(= :item (:tag %))))


  ; seems that xml-seq makes it a lot easier to work with clojure fn's to work with parsed xml by turning it into a tree

  ; https://clojuredocs.org/clojure.core/xml-seq
  ;; (require '[clojure.java.io :as io])
  ;; (require '[clojure.data.xml :as xml])

  (def feeds
    [[:tweakers "https://tweakers.net/feeds/mixed.xml"]])

  (pmap
   (fn [[feed url]]
     (let [content (comp first :content)]
       [feed
        (sequence
         (comp
          (filter (comp string? content))
          (filter (comp #{:title} :tag))
          #_(filter #(re-find #"(?i)breaking" (content %)))
          (map content))
         (xml-seq (xml/parse (io/input-stream url))))]))
   feeds)


  (def parsed-xml (xml/parse (io/input-stream (second (first feeds)))))
  (def seqd-xml (xml-seq parsed-xml))

  (def content (comp first :content))

  (map content parsed-xml)
  (keys parsed-xml)
  (content parsed-xml)
  (filter (comp #{:title} :tag) (content parsed-xml))

  (map content seqd-xml)
  (keys seqd-xml)
  (content seqd-xml)
  (filter (comp #{:title} :tag) (map content seqd-xml))
  (filter (comp string? content) (filter (comp #{:title} :tag) (map content seqd-xml)))


  (defn a [url]
    (sequence
     (comp
      #_(filter (comp string? content))
      (filter (comp #{:title} :tag))
      (map (comp first :content)))
     (xml-seq (xml/parse (io/input-stream url)))))

  (a (second (first feeds)))

  (def b (comp
          #_(filter (comp string? content))
          (filter (comp #{:title} :tag))
          (map (comp first :content))))

  (b (xml-seq (xml/parse (io/input-stream (second (first feeds))))))


  (def feed "https://tweakers.net/feeds/mixed.xml")
  (def x (-> feed
             (clojure.java.io/input-stream)
             (clojure.data.xml/parse :namespace-aware false)))

  (clojure.pprint/pprint x)

  (count x)

  (def x-seq (xml-seq x))

  (count x-seq)

  (filter (comp #{:title} :tag) x-seq)
  (filter (comp #{:item} :tag) x-seq)

  (for [xs (xml-seq x)
        :let [xy (:content xs)
              content (comp first :content first)] ; fn
        :when (= :item (:tag xs))]
    [:title (content (filter (comp #{:title} :tag) xy))
     :hmtl-content (content (filter (comp #{:content:encoded} :tag) xy))
     ])

  (->> x
       (:content) ; rss
       (first)
       (:content) ; channel
      ;;  (filter #(= :title (:tag %))) ; one way
       (filter (comp #{:title} :tag)) ; cleaner
       #_(map :content))

  (def items
    (->> x
         (:content) ; rss
         (first)
         (:content) ; channel
         #_(filter #(= :item (:tag %)))
         (filter (comp #{:item} :tag))
         (map :content)))

  (def key-set #{:title :link :content:encoded})

  (defn item-cleaner
    [item-seq]
    (filter (comp key-set :tag) item-seq))

  (def cleaned-items
    (map item-cleaner items))

  (count items)

  items

  (def f (first items))

  (count f)

  (def clean-item
    (filter (comp key-set :tag) f))

  (map :tag f)

  (def ff (first f))
  (:tag ff)

  (def g (first cleaned-items))

  (clojure.pprint/pprint f)
  (clojure.pprint/pprint g)
  (clojure.pprint/pprint clean-item)


  )
