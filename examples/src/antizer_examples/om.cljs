(ns antizer-examples.om
  (:require [clojure.string :as string]
            ;; each language has to be required seperately
            ;; in order for the display to be correct
            [cljsjs.moment]
            [cljsjs.moment.locale.es]
            [cljsjs.moment.locale.de]
            [cljsjs.moment.locale.ja]
            [cljsjs.moment.locale.ru]
            [cljsjs.moment.locale.zh-cn]
            [goog.dom :as gdom]
            [om.dom :as dom]
            [om.next :as om :refer [defui]]
            [antizer.om :as ant]
            [antizer-examples.common :as common]))

(defui Card
  Object
  (render [this]
          (dom/div nil 
                   (dom/h2 nil "Cards")
                   (ant/card {:title "Title" :bordered true :class "card"}
                             (dom/p nil "Not the usual lorem ipsum")) (dom/br nil)
                   (ant/card {:bordered true :class "card-photo"} 
                             (dom/div nil (dom/img #js {:src "https://unsplash.it/330/120/?random"}))
                             (ant/col {:span 12} (dom/div nil (dom/h3 nil "Please rate me")))
                             (ant/col {:span 12} (ant/rate))))))
(def card (om/factory Card))

(defui ToolTip
  Object
  (render [this]
  (dom/div #js {:className "example-button"}
           (dom/h2 nil "Tooltips and Popups "
                   (ant/tooltip {:title "Found me!"} 
                                (ant/icon {:type "question-circle-o" :style {:font-size 13}})))
           (ant/tooltip {:title "Tooltip"} (ant/button "Tooltip"))
           (ant/popover {:content "Dum dee dee dum dee dee dum" :title "Deedee dum"} (ant/button "Popover"))
           (ant/popconfirm {:title "Are you sure?"
                            :on-confirm #(ant/message-success "You clicked OK")
                            :on-cancel #(ant/message-error "You clicked Cancel")}
                           (ant/button "Click to confirm")))))
(def tooltip (om/factory ToolTip))

(defui Notifications
  Object
  (render [this]
          (dom/div #js {:className "example-button"}
                   (dom/h2 nil "Notifications")
                   (ant/button {:on-click #(ant/notification-open 
                                            {:message "Timed Notification"
                                             :description "This notification will close after 4.5 seconds"})} 
                               "Notification")
                   (ant/button {:on-click 
                                #(let [key (random-uuid)]
                                   (ant/notification-open
                                    {:message "Popup Notification"
                                     :duration 0
                                     :btn (ant/button {:type "primary" 
                                        :on-click (fn [] (ant/notification-close key))} 
                                                      "Click to dismiss")
                                     :key key
                                     :description "This notification will not close until it is dismissed"}))} 
                               "Popup Notification"))))
(def notifications (om/factory Notifications))

(defui Carousel
  Object
  (render [this]
          (dom/div nil
           (dom/h2 nil "Carousel")
           (ant/carousel {:autoplay true :dots true}
                         (for [i (range 3)]
                           (dom/div #js {:key i} (dom/img #js {:src (str "https://unsplash.it/400/300/?random=" i)})))))))
(def carousel (om/factory Carousel))

(defui Buttons
  Object
  (render [this]
          (dom/div #js {:className "example-button"}
                   (dom/h2 nil "Buttons")
                   (ant/button {:type "primary"} "Primary")
                   (ant/button "Default")
                   (ant/button {:type "danger"} "Warning")
                   (ant/button {:icon "shopping-cart" :type "primary"} "With icon")
                   (ant/button {:icon "edit" :type "primary"}))))
(def buttons (om/factory Buttons))

(defui Messages
  Object
  (render [this]
          (dom/div #js {:className "example-button"}
                   (dom/h2 nil "Messages")
                   (ant/button {:on-click #(ant/message-info "Normal message")} "Normal")
                   (ant/button {:on-click #(ant/message-success "Success message")} "Success")
                   (ant/button {:on-click #(ant/message-warn "Warning message")} "Warning")
                   (ant/button {:on-click #(ant/message-error "Error message")} "Error")
                   (ant/button {:on-click #(ant/message-loading "This message will disappear in 10 seconds" 10)} "Timed"))))
(def messages (om/factory Messages))

(defui TimeLine
  Object
  (render [this]
          (dom/div nil
                   (dom/h2 nil "Timeline")
                   (ant/timeline
                    (ant/timeline-item {:color "red"} "6th June Project created")
                    (ant/timeline-item {:color "blue"} "8th June Initial prototype done")
                    (ant/timeline-item {:color "green"} "20th June Final release")))))
(def timeline (om/factory TimeLine))

(defn render-example 
  "Render each example within a bordered box"
  [examples]
  ;; we need to generate a different key for each react element
  (ant/col {:span 12}
    (for [example examples]
      (dom/div #js {:className "box"}
        (dom/div #js {:className "box-content"}
          (example))))))

(defn render-full-row
  [example]
  (ant/col {:span 24}
    (dom/div #js {:className "box"}
      (dom/div #js {:className "box-content"}
        (example)))))

(defui ContentArea
  Object
  (render [this]
          (ant/layout-content {:class "content-area"}
                              (ant/row {:gutter 12}
                                       (render-example [carousel buttons messages timeline])
                                       (render-example [card tooltip notifications])
                                       ;(render-example [carousel buttons messages timeline tree progress])
                                       ;(render-example [card tooltip notifications auto-complete localization modal avatar])
                                       )
                              ;(render-full-row form-example)
                              ;(render-full-row datatable)
                              )))

(def content-area (om/factory ContentArea))

(defui SideMenu
  Object
  (render [this]
          (ant/menu {:mode "inline" :theme :dark :style {:height "100%"}}
                    (ant/menu-item {:disabled true} "Menu without icons")
                    (ant/menu-item "Menu Item")
                    (ant/menu-sub-menu {:title "Sub Menu"}
                                       (ant/menu-item "Item 1")
                                       (ant/menu-item "Item 2"))
                    (ant/menu-item {:disabled true} "Menu with icons")
                    (ant/menu-item [:span {:key "s"} (ant/icon {:type "home"}) "Menu Item"])
                    (ant/menu-sub-menu {:title [:span (ant/icon {:type "setting"}) "Sub Menu"]}  
                                       (ant/menu-item [:span {:key "s"} (ant/icon {:type "user"}) "Item 1"])
                                       (ant/menu-item [:span {:key "s"} (ant/icon {:type "notification"}) "Item 2"])))))
(def side-menu (om/factory SideMenu))

(defui RenderLayout
  Object
  (render [this]
          (ant/locale-provider {:locale (ant/locales "en_US")}
                               (ant/layout
                                (ant/affix
                                 (ant/layout-header {:class "banner"}
                                                    (ant/row
                                                     (ant/col {:span 12} (dom/h2 #js {:className "banner-header"} "Antizer Om Example"))
                                                     (ant/col {:span 1 :offset 11} 
                                                              (dom/a #js {:href "https://github.com/priornix/antizer"} 
                                                                     (ant/icon {:class "banner-logo" :type "github"}))))))
                                (ant/layout
                                 (ant/layout-sider (side-menu))
                                 (ant/layout {:style {:width "60%"}} 
                                             (content-area)))))))
(def render-layout (om/factory RenderLayout))

(defn init! []
  (js/ReactDOM.render (render-layout)
    (gdom/getElement "app")))
