
(ns crash-screech.check-data

  (:require [clojure.string :as clj-str])

  (:require [crash-screech.years-months  :refer [adjusted-date instant-time-fn]])
  (:require [crash-screech.config-args :refer [compact-hash]])

  (:require [global-consts :refer :all]))

(defn count-string
  "has test"
  [hay-stack needle-regex]
;(println "hay-stack" hay-stack  (type hay-stack))
;(println "needl-reg" needle-regex (type needle-regex))
  (let [split-vector (clj-str/split hay-stack needle-regex)
        occurance-count (dec (count split-vector))]
    occurance-count))

(defn trunc-string
  "has test"
  [the-string num-chars]
  (if the-string
    (subs the-string 0 (min (count the-string) num-chars))
    EMPTY-HTML))

(defn derive-data
  "has test"
  [check-record]
  (let [{:keys [the-url the-date the-html the-accurate the-time]} check-record
        check-url the-url
        check-accurate the-accurate
        check-bytes (count the-html)
        check-html (trunc-string the-html ERROR-KEEP-LENGTH)
        check-date (adjusted-date the-date)
        check-time the-time
        new-record (compact-hash check-url
                                 check-date
                                 check-bytes
                                 check-html
                                 check-accurate
                                 check-time)]
    new-record))

(defn uniquely-id
  "has test"
  [many-index many-item]
  (let [the-date (:check-date many-item)
        extended-date (str the-date "+" many-index)
        unique-item (assoc-in many-item [:_id] extended-date)]
    unique-item))

(defn ensure-has-date
  "has test"
  [check-record]
  (if (check-record :the-date)
    check-record
    (let [now-date (instant-time-fn)
          dated-check (assoc-in check-record [:the-date] now-date)]
      dated-check)))

(defn prepare-data
  "has test"
  [check-records]

;(println "check-records" check-records  (type check-records))
;(println "needl-reg" needle-regex (type needle-regex))



  (let [records-dated (for [check-record check-records]
                        (ensure-has-date check-record))
        derived-data (for [dated-record records-dated]
                       (derive-data dated-record))
        unique-data (map-indexed uniquely-id derived-data)]
    unique-data))