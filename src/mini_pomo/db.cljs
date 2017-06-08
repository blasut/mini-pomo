(ns mini-pomo.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::greeting string?)
(s/def ::app-db
  (s/keys :req-un [::time ::timer ::timer-running]))


(s/def ::time (s/and int? #(= % 1500)))
(s/def ::not-running
  (s/keys :req-un [::time ::timer-running]))


(s/def ::timer-running boolean?)

;; Maybe spec with different names?
(s/def ::pomodoro-time (s/and int? #(>= % 0) #(<= % 1500)))
(s/def ::pomodoro-running
  (s/keys :req-un [::pomodoro-time ::timer-running]))

;; State can be:
;; Not running:
;;   - Should show 25:00 in red
;;   - Should show the start-button

;; Pomodoro running:
;;   - Should count down from 25:00 towards 0
;;   - Should show the stop button
;;   - Should be showing the time in red  

;; Pause running:
;;   - Should count down from 5 or 15 minutes towards 0
;;   - Should show the stop button
;;   - Should be showing the time in green  

(def init {:time (* 25 60)
           :timer nil
           :timer-running false})

(def pomodoro-running {:timer-running true
                       :timer (not nil)})

(def pause-running {:timmer-running true
                    :timer (not nil)})

;; initial state of app-db
(def app-db {:time (* 25 60)
             :timer nil
             :timer-running false})
