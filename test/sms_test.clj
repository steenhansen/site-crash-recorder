
; /test/test_sms.clj

; (-test-sms HEROKU_CONFIG)
; (-test-sms HEROKU_CONFIG false)
; (-test-sms HEROKU_CONFIG true USE_ENVIRONMENT)

(ns sms-test

  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.data-store :refer [build-db]])
  (:require [crash-sms.sms-event :refer [sms-to-phones]]))

(comment "to send sms message to phone"
         (-test-sms HEROKU_CONFIG)
         (-test-sms HEROKU_CONFIG USE_ENVIRONMENT))

(defn -test-sms
  ([config-file] (-test-sms config-file true))

  ([config-file testing-sms?] (-test-sms config-file testing-sms? IGNORE-ENV-VARS))

  ([config-file testing-sms? environment-utilize]
   (let [the-check-pages (make-check-pages 0)
         [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                    the-check-pages
                                    USE_MONGER_DB
                                    config-file
                                    environment-utilize)]
     (sms-to-phones sms-data testing-sms?))))
