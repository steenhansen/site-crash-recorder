(ns sff-audio.singular-service)

(defonce ^:dynamic *service-kill-list* (atom {}))

(defn add-service
  [spawning-name kill-service]
  (let [kill-list @*service-kill-list*
        spawning-key (keyword spawning-name)
        new-kills (assoc-in kill-list [spawning-key] kill-service)]
    (reset! *service-kill-list* new-kills)))

(defn remove-service
  [spawning-name]
  (let [kill-list @*service-kill-list*
        spawning-key (keyword spawning-name)
        service-running? (contains? kill-list spawning-key)]
    (if service-running?
      (let [kill-service (spawning-key kill-list)] (kill-service))
      (let [new-kills (dissoc kill-list spawning-key)]
        (reset! *service-kill-list* new-kills)))))

(comment "to stop all services" (kill-services))
(defn kill-services
  []
  (let [kill-list @*service-kill-list*]
    (println "Killing all services:")
    (for [[service-key kill-service] kill-list] (kill-service))))