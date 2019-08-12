

(ns tests-check-data
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.check-data :refer :all])

  (:require [java-time :refer [local-date?]])


  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))

(deftest uni-count-string
  (testing "count-string : cccccccccccccccccccccc "
    (let [occurance-count (count-string "001001001000" #"1")]
      (console-test "uni-count-string" "check-data")
      (is (= occurance-count 3)))))

(spec-test/instrument 'count-string)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 

(def ^:const T-BEFORE-THE-DATA {:the-url "www.sffaudio.com",
                                :the-date "2019-06-19-01:54:03.800Z",
                                :the-html "123456789",
                                :the-accurate true,
                                :the-time 1234})

(def ^:const T-AFTER-CHECK-DATA {:check-url "www.sffaudio.com",
                                 :check-date "2019-06-19-01-54-03.800Z",
                                 :check-html "123456789",
                                 :check-bytes 9
                                 :check-accurate true,
                                 :check-time 1234})

(deftest uni-derive-data
  (testing "test-dervive-data : cccccccccccccccccccccc "
    (let [derived-data (derive-data T-BEFORE-THE-DATA)]
      (console-test "uni-derive-data" "check-data")
      (is (= derived-data T-AFTER-CHECK-DATA)))))


;;;;;;;;;;;;;;


(def ^:const T-ENSURE-DATA {:the-url "www.sffaudio.com",
                            :the-html "123456789",
                            :the-accurate true,
                            :the-time 1234})

(deftest uni-ensure-has-date
  (testing "est-ensure-has-date : DDDDDDDDDDDDD "
    (let [has-date (ensure-has-date T-ENSURE-DATA)]
      (console-test "uni-ensure-has-date" "check-data")
      (is (contains? has-date :the-date)))))


;;;;;;;;;;;;;;;;;;;;


(def ^:const T-BEFORE-ENSURE-DATA

  [{:the-url "www.sffaudio.com",
    :the-date "2019-06-19-01:54:03.800Z",
    :the-html "blah 1111",
    :the-accurate true,
    :the-time 1234}
   {:the-url "sffaudio.herokuapp.com_rsd_rss",
    :the-date "2019-06-19-01:54:03.800Z",
    :the-html "bluh 2222",
    :the-accurate true,
    :the-time 12346}])

(def ^:const T-AFTER-ENSURE-DATA

  [{:check-url "www.sffaudio.com",
    :check-date "2019-06-19-01-54-03.800Z",
    :_id "2019-06-19-01-54-03.800Z+0",
    :check-html "blah 1111",
    :check-bytes 9
    :check-accurate true,
    :check-time 1234}
   {:check-url "sffaudio.herokuapp.com_rsd_rss",
    :_id "2019-06-19-01-54-03.800Z+1",
    :check-date "2019-06-19-01-54-03.800Z",
    :check-html "bluh 2222",
    :check-bytes 9
    :check-accurate true,
    :check-time 12346}])

(deftest uni-prepare-data
  (testing "prepare-tests-data : fffff "
    (let [prepared-data (prepare-data T-BEFORE-ENSURE-DATA)]
      (console-test "uni-prepare-data" "check-data")
      (is (= prepared-data T-AFTER-ENSURE-DATA)))))

;;;;;;;;;;;;;


(deftest uni-trunc-string
  (testing "test-trunc-string : cccccccccccccccccccccc "
    (let [trunced-str (trunc-string "123456789" 3)]
      (console-test "uni-trunc-string" "check-data")
      (is (= trunced-str "123")))))

;;;;;;;;;;;;

(def ^:const T-BEFORE-UNIQUE-ID {:check-url "www.sffaudio.com",
                                 :check-date "2019-06-19-01:54:03.800Z",
                                 :check-html "123456789",
                                 :check-bytes 9
                                 :check-accurate true,
                                 :check-time 1234})

(def ^:const T-AFTER-UNIQUE-ID {:check-url "www.sffaudio.com",
                                :check-date "2019-06-19-01:54:03.800Z",
                                :_id "2019-06-19-01:54:03.800Z+1",
                                :check-html "123456789",
                                :check-bytes 9
                                :check-accurate true,
                                :check-time 1234})

(deftest uni-uniquely-id
  (testing "test-uniquely-id : cccccccccccccccccccccc "
    (let [unique-data (uniquely-id 1 T-BEFORE-UNIQUE-ID)]
      (console-test  "uni-uniquely-id"  "check-data")
      (is (= unique-data T-AFTER-UNIQUE-ID)))))





