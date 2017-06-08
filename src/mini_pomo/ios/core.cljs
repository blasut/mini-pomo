(ns mini-pomo.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [mini-pomo.events]
            [mini-pomo.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
      (.alert (.-Alert ReactNative) title))


(defn zero-pad [x]
  (if (< x 10)
      (str "0" x)
      (str x)))

(defn seconds->minutes [seconds]
  (let [minutes (zero-pad (quot seconds 60))
        seconds (zero-pad (rem seconds 60))]
    (str minutes ":" seconds)))

(defn clock []
  (let [seconds-elapsed (subscribe [:time])]
    (fn []
      [view
        [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} "Seconds left: " @seconds-elapsed]
        [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} "MM:SS left: " (seconds->minutes @seconds-elapsed)]])))

(defn dispatch-timer-event []
  (dispatch [:timer-tick]))

(defn start-timer []
  (dispatch [:timer-start (js/setInterval dispatch-timer-event 1000)]))

(defn stop-timer []
  (dispatch [:timer-stop]))

(defn app-root []
  (let [timer-running? (subscribe [:timer-running])
        time (subscribe [:time])
        timer (subscribe [:timer])]
    (fn []
     [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
      [text timer-running?]
      [text timer]
      [clock]

      (if @timer-running?
        [touchable-highlight {:style {:background-color "red" :padding 10 :border-radius 5} :on-press (fn [e] (stop-timer))}
          [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "stop"]]
        [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5} :on-press (fn [e] (start-timer))}
          [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "start"]])])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "MiniPomo" #(r/reactify-component app-root)))
