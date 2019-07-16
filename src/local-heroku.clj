; 
(comment "to start"
         (-local-heroku "monger-db" "../heroku-config.edn" "use-environment"))
(defn -local-heroku
  [db-type config-file environment-utilize]
  (kill-services)
  (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TABLE-NAME
                                                         THE-CHECK-PAGES
                                                         db-type
                                                         config-file
                                                         environment-utilize)
        int-port (Integer/parseInt web-port)
        temporize-func
        (single-cron-fn scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data)
        request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data)]
    (web-init int-port request-handler)))