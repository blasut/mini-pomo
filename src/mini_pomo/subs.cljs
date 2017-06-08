(ns mini-pomo.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :time
  (fn [db _]
    (-> db
       :time)))

(reg-sub
  :timer-running
  (fn [db _]
    (-> db
       :timer-running)))

(reg-sub
  :timer
  (fn [db _]
    (-> db
       :timer)))
