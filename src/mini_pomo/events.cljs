(ns mini-pomo.events
  (:require
   [re-frame.core :refer [reg-event-db after debug]]
   [clojure.spec.alpha :as s]
   [mini-pomo.db :as db :refer [app-db]]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

(def debug-interceptors
  [validate-spec
   (.log js/console "lol" debug)])

;; -- Handlers --------------------------------------------------------------

(reg-event-db
 :initialize-db
 debug-interceptors
 (fn [_ _]
   app-db))

(reg-event-db
 :timer-start        
 debug-interceptors
 (fn [db [_ timer]]
   (assoc db
          :timer timer
          :timer-running true))) 

(reg-event-db
 :timer-stop        
 debug-interceptors
 (fn [db _]
   (assoc db
          :time (:time app-db)
          :timer-running false
          :timer (.clearInterval js/window (:timer db))))) 

(reg-event-db
 :timer-tick        
 debug-interceptors
 (fn [db _]
   (let [curr-time (:time db)
         new-time (- curr-time 1)]
     (if (= new-time 0)
       (assoc db :time 0) 
       (assoc db :time new-time))))) 
