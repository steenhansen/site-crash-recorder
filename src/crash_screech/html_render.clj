
(ns crash-screech.html-render
  (:require [ring.util.response :as ring-response])
  (:require [net.cgrand.enlive-html :as enlive-html])
  (:require [clojure.string :as clj-str])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.years-months  :refer [current-yyyy-mm current-month
                                                 prev-month prev-yyyy-mm date-to-yyyy-mm]])
;  (:require [java-time.local :as j-time])
  (:require [global-consts-vars :refer :all]))

(defn render-parts [html-pieces] (clj-str/join html-pieces))

(defn day-hour-min
  "has test"
  [check-date]
  (let [dd-hh-mm (subs check-date 8 16)
        [days hours minutes] (clj-str/split dd-hh-mm #"-")
        short-date (str days "-" hours ":" minutes)]
    short-date))

(defn fill-accurate
  [check-accurate]
  (enlive-html/do-> (enlive-html/content (if check-accurate "" "FAIL"))
                    (if check-accurate
                      (enlive-html/add-class "status_good")
                      (enlive-html/add-class "status_bad"))))
(declare fill-time)
(declare fill-date)

(defn fill-url [check-url] (enlive-html/do-> (enlive-html/content check-url)))

(declare fill-bytes)

(defn fill-html
  [check-accurate check-html]
  (enlive-html/do-> (enlive-html/content (if check-accurate "" check-html))))

(defn get-two-months
  "has db test"
  [my-db-obj yyyy-mm]
  (let [get-all (:get-all my-db-obj)
        this-y-m (current-yyyy-mm yyyy-mm)
        last-y-m (prev-yyyy-mm yyyy-mm)
        this-months (get-all this-y-m)
;_ (println "66666666666 get-two-mongths last-y-m yyyy-mm" last-y-m yyyy-mm)
        last-months (get-all last-y-m)
        current-months (vec this-months)
        previous-months (vec last-months)]
    [previous-months current-months]))

(defn get-index
  "has db test"
  ([my-db-obj] (get-index my-db-obj (date-to-yyyy-mm (java-time.temporal/instant))))
  ([my-db-obj yyyy-mm]
   (let [[previous-months current-months] (get-two-months my-db-obj yyyy-mm)
         prev-name (prev-month yyyy-mm)
         cur-name (current-month yyyy-mm)
         table-name (:table-name my-db-obj)
         db-data {:page-title "SFFaudio page checks",
                  :month-sections
                  [{:month-type prev-name, :month-data previous-months}
                   {:month-type cur-name, :month-data current-months}]}]

     (defn fill-time
       [check-time]
       (if  (= DB-TABLE-NAME table-name)

         (enlive-html/do-> (enlive-html/content (str check-time))
                           (if (> check-time 2000)
                             (enlive-html/add-class "speed_bad")
                             (if (> check-time 1000)
                               (enlive-html/add-class "speed_average")
                               (enlive-html/add-class "speed_good"))))

         (enlive-html/do-> (enlive-html/content (str FAKE-SCRAPE-SPEED))
                           (enlive-html/add-class "speed_test"))))

     (defn fill-date  [check-date]
       (if  (= DB-TABLE-NAME table-name)
         (enlive-html/do-> (enlive-html/content (day-hour-min check-date)))
         (enlive-html/do-> (enlive-html/content (str FAKE-TEST-DATE)))))

     (defn fill-bytes [check-bytes]
       (if  (= DB-TABLE-NAME table-name)
         (enlive-html/do-> (enlive-html/content (str check-bytes))))
       (enlive-html/do-> (enlive-html/content (str FAKE-SCRAPE-BYTES))))

     (enlive-html/defsnippet row-selector
       BASE-HTML-TEMPLATE
       [[:.month_content (enlive-html/nth-of-type 1)] :>
        enlive-html/first-child]
       [{:keys [check-url check-date check-bytes check-html
                check-accurate check-time]}]
       [:div.scrape_accurate]
       (fill-accurate check-accurate)
       [:div.scrape_time]
       (fill-time check-time)
       [:div.scrape_date]
       (fill-date check-date)
       [:div.scrape_url]
       (fill-url check-url)
       [:div.scrape_bytes]
       (fill-bytes check-bytes)
       [:div.scrape_html]
       (fill-html check-accurate check-html))

     (enlive-html/defsnippet month-selector
       BASE-HTML-TEMPLATE
       {[:.a_month] [[:.month_content
                      (enlive-html/nth-of-type 1)]]}
       [{:keys [month-type month-data]}]
       [:.a_month]
       (enlive-html/content month-type)
       [:.month_content]
       (enlive-html/content (map row-selector month-data)))

     (enlive-html/deftemplate index-page
       BASE-HTML-TEMPLATE
       [{:keys [page-title month-sections]}]
       [:#title_of_page]
       (enlive-html/content page-title)
       [:#two_months]
       (enlive-html/content (map month-selector
                                 month-sections)))

     (render-parts (index-page db-data)))))
