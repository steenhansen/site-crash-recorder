

;   a

; https://github.com/clojure/tools.logging   => log4j.properties
; http://www.paullegato.com/blog/logging-clojure-clj-logging-config/

(ns prepare-tests
  (:require [clj-logging-config.log4j :as log-config]
            [clojure.tools.logging :as log])
  (:require [clj-http.client :as http-client])

  (:require [clojure.string :as clj-str])

  (:require [global-consts  :refer :all])

  (:require [crash-screech.choose-db :refer :all]))

(def ^:const DB-TEST-NAME "zzz3")
(def ^:const CONSOLE-TESTS true)

(def ^:const DO-DYNAMODB-TESTS false)

(defn console-test
  ([test-name] (console-test test-name "-"))
  ([test-name test-area]
   (if CONSOLE-TESTS
     (println "..." test-area test-name ))))

(defn dampen-mongo-logging
  []
  (log-config/set-logger!
   :level :debug
   :out (org.apache.log4j.FileAppender.
         (org.apache.log4j.EnhancedPatternLayout.
          org.apache.log4j.EnhancedPatternLayout/TTCC_CONVERSION_PATTERN)
         "logs/foo.log"
         true)))

(defn local-dynamodb-on?
  []
  (if DO-DYNAMODB-TESTS
    (try
      (http-client/get "http://localhost:8000/shell/")
      (catch Exception e
        (throw  (Exception. " **** ERROR DynamoDB is not running on - http://localhost:8000"))))))

(defn local-mongo-on?
  []
  (try (http-client/get "http://localhost:27017")
       (catch Exception e
         (throw
          (Exception.
           " **** ERROR MongoDB is not running on - http://localhost:27017")))))

(defn strip-white-space [my-text] (clj-str/replace my-text #"\s" ""))

(defn reset-test-to-actual-data
  [test-data actual-data]
  (spit test-data actual-data))

(defn print-block
  []
  (println
   "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"))

(defn sms-is-in-test [db-type]
  (let [[_ _ _ sms-data] (build-db DB-TEST-NAME
                                   []
                                   db-type
                                   TEST-CONFIG-FILE
                                   IGNORE-ENV-VARS)
        testing-sms? (:testing-sms? sms-data)]
    (if (not testing-sms?)  (throw
                             (Exception.
                              " **** ERROR :TESTING_SMS is NOT false")))))

(defn is-string-number
  [num-as-str]
  (try
    (let [the-number (Integer/parseInt num-as-str)
          is-number (number? the-number)
          bigger-0 (< 0 the-number)]
      (and is-number bigger-0))
    (catch Exception e
      false)))

(defn is-url-dir
  [url-dir-leading-slash]
  (try
    (let [the-slash (first url-dir-leading-slash)
          the-dir (subs url-dir-leading-slash 1)]
      (and (= the-slash "/") (string? the-dir)))
    (catch Exception e
      false)))