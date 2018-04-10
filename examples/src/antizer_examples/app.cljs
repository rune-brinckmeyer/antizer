(ns antizer-examples.app
  (:require [antizer-examples.reagent :as reagent]
            [antizer-examples.rum :as rum]
            [antizer-examples.om :as om]
            [cljsjs.moment]))

(defn init! []
  (case js/example
    "reagent" (reagent/init!)
    "rum" (rum/init!)
    "om" (om/init!)))

(enable-console-print!)
(init!)
