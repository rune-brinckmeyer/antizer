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
            [goog.object :refer [getValueByKeys]]
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

(def auto-complete (om/factory
                    (om/ui
                     Object
                     (componentWillMount [this]
                                         (om/set-state! this {:auto-data ["om","reagent" "rum"]}))
                     (render [this]
                             (let [data (:auto-data (om/get-state this))]
                               (dom/div nil
                                        (dom/h2 nil (str "Autocomplete"))
                                        (ant/auto-complete
                                         {:style {:width "80%"}
                                          ;; we need to use dataSource instead of data-source, see README.MD
                                          :dataSource data
                                          :on-search
                                          (fn [x]
                                            (om/set-state! this {:auto-data
                                                                 (take 3 (iterate #(str % (string/reverse %)) x))}))
                                          :placeholder "Enter something"})))))))

(def localization (om/factory
                   (om/ui
                    Object
                    (componentWillMount [this]
                                         (om/set-state! this {:locale-data "en_US"}))
                    (render [this]
                            (let [locale (:locale-data (om/get-state this))]
                              (ant/locale-provider {:locale (ant/locales locale)}
                                                   (ant/col
                                                    (dom/h2 nil "Localization")
                                                    (dom/span nil "Choose a language:" 
                                                              (ant/select {:default-value "en_US"
                                                                           :on-change #(let [locale-val (if (= % "zh_CN") nil %)]
                                                                                         (om/set-state! this {:locale-data locale-val}))
                                                                           :style {:padding "10px"}}
                                                                 (ant/select-option {:value "en_US"} "English")
                                                                 (ant/select-option {:value "es_ES"} "Español")
                                                                 (ant/select-option {:value "de_DE"} "Deutsch")
                                                                 (ant/select-option {:value "ru_RU"} "Русский")
                                                                 (ant/select-option {:value "zh_CN"} "中文")
                                                                 (ant/select-option {:value "ja_JP"} "日本語")
                                                                 (ant/select-option {:value "tlh" :disabled true} "Klingon")))
                                                    (ant/pagination {:total 40 :show-size-changer true}) (dom/br nil)(dom/br nil)
                                                    (ant/date-picker {:format "ddd MMM Do YYYY" :default-value (js/moment) :style {:width "60%"} :allow-clear false :show-today false}) (dom/br nil) (dom/br nil)
                                                    (ant/time-picker {:style {:width "60%"}}) (dom/br nil)
                                                    (ant/calendar {:fullscreen false :default-value (js/moment)})
                                                    (ant/table {:columns common/columns}))))))))

(def avatar (om/factory
             (om/ui
              Object
              (render [this]           
                      (dom/div #js {:className "avatar"}
                               (dom/h2 nil "Avatar")
                               (dom/div nil
                                        (ant/avatar {:style {:background-color "#87d068"} :icon "user" :class "va-middle"})
                                        (ant/avatar {:style {:color "#f56a00" :background-color "#fde3cf"} :class "va-middle"} "U")
                                        (ant/avatar {:style {:background-color "#ffbf00"} :class "va-middle"} "John")
                                        (ant/badge {:count 10} (ant/avatar {:style {:background-color "#108ee9"} :shape "square" :icon "user"}))))))))

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

(def tree (om/factory
           (om/ui
            Object
            (render [this]
                    (dom/div nil
                     (dom/h2 nil "Tree")
                     (ant/tree {:checkable true :default-expanded-keys ["functional" "clr" "jvm" "javascript" "nodejs"]
                                :default-checked-keys ["clojure" "clojure-clr" "cljs" "lumo" "planck"]}
                               (ant/tree-tree-node {:title "Functional Languages" :key "functional"}
                                                   (ant/tree-tree-node {:title "CLR" :key "clr"}
                                                                       (ant/tree-tree-node {:title "Clojure CLR" :key "clojure-clr"}))
                                                   (ant/tree-tree-node {:title "Haskell" :key "haskell"})
                                                   (ant/tree-tree-node {:title "JVM" :key "jvm"}
                                                                       (ant/tree-tree-node {:title "Clojure" :key "clojure"})
                                                                       (ant/tree-tree-node {:title "Frege" :key "frege"})
                                                                       (ant/tree-tree-node {:title "Scala" :disable-checkbox true}))
                                                   (ant/tree-tree-node {:title "JavaScript Engine" :key "javascript"}
                                                                       (ant/tree-tree-node {:title "ClojureScript" :key "cljs"}))
                                                   (ant/tree-tree-node {:title "Node.js" :key "nodejs"}
                                                                       (ant/tree-tree-node {:title "Lumo" :key "lumo"}))
                                                   (ant/tree-tree-node {:title "Planck" :key "planck"}))))))))

(def progress (om/factory
               (om/ui
                Object
                (componentWillMount [this]
                                    (om/set-state! this {:percent-data 50}))
                (render [this]
                        (let [percent (:percent-data (om/get-state this))
                              operate (fn [operation]
                                        (if (= :plus operation)
                                          (if (< percent 100) (om/set-state! this {:percent-data (+ percent 10)}))
                                          (if (>= percent 0) (om/set-state! this {:percent-data (- percent 10)}))))
                              status (cond
                                       (< percent 42) "exception"
                                       (= percent 100) "success" 
                                       :else "active")]
                                        (dom/div #js {:className "progress"}
                                                 (dom/h2 nil "Progress")
                                                 (ant/progress {:type "circle" :percent percent :status status})
                                                 (ant/button-group
                                                  (ant/button {:icon "plus" :on-click #(operate :plus)})
                                                  (ant/button {:icon "minus" :on-click #(operate :minus)}))
                                                 (ant/progress {:percent percent :status status
                                                                :style {:width "42%"}})))))))

(defn handle-fields-change[comp]
  (om/update-state! comp assoc :form (ant/get-form comp)))

(defui UserForm
  Object
  (componentWillMount [this]
                      (om/set-state! this (-> (getValueByKeys this "props")
                                              (js->clj :keywordize-keys true))))
  (render [this]
          (let [form (ant/get-form this)
                display-buttons? (:display-buttons? (om/get-state this))]
            (ant/form {:layout "horizontal"}
                      (ant/form-item (merge common/form-style {:label "Name"})
                                     (ant/decorate-field form "name" {:rules [{:required true}]}
                                                         (ant/input {:on-change #(handle-fields-change this)})))
                      (ant/form-item (merge common/form-style {:label "Email"})
                                     (ant/decorate-field form "email" {:rules [{:required true} {:type "email"}]} 
                                                         (ant/input {:on-change #(handle-fields-change this)})))
                      (ant/form-item (merge common/form-style {:label "Address"})
                                     (ant/decorate-field form "address" {:initial-value "Some initial value" :rules [{:required true}]} 
                                                         (ant/input {:on-change #(handle-fields-change this)})))
                      (ant/form-item (merge common/form-style {:label "Years of Experience"})
                                     (ant/decorate-field form "experience" {:rules [{:required true}]} 
                                                         (ant/radio-group {:on-change #(handle-fields-change this)}
                                                          (ant/radio {:value 1} "1-10")
                                                          (ant/radio {:value 10} "10-30")
                                                          (ant/radio {:value 30} "30-50")
                                                          (ant/radio {:value 50} "> 50"))))
                      (ant/form-item (merge common/form-style {:label "Start Date"})
                                     (ant/decorate-field form "date" {:initial-value (js/moment) :rules [{:required true}]}
                                                         (ant/date-picker {:format "MMM Do YYYY" :on-change #(handle-fields-change this)})))
                      (ant/form-item (merge common/form-style {:label "Accept Terms?"})
                                     (ant/decorate-field form "accept-terms"
                                                         (ant/switch {:on-change #(handle-fields-change this)})))
                      (if display-buttons?
                        (ant/form-item {:wrapper-col {:offset 6}}
                                       (ant/col {:span 4}
                                                (ant/button {:type "primary" :on-click #(do(ant/validate-fields form) (handle-fields-change this))}
                                                            "Submit"))
                                       (ant/col {:offset 1}
                                                (ant/button {:on-click #(do (ant/reset-fields form) (handle-fields-change this))}
                                                            "Reset"))))))))

(def modal (om/factory
                   (om/ui
                    Object
                    (componentWillMount [this]
                                        (do (om/set-state! this (-> (getValueByKeys this "props")
                                                                    (js->clj :keywordize-keys true)))
                                            (om/update-state! this assoc :modal1 false)
                                            (om/update-state! this assoc :modal-form false)))
                    (render [this]
                            (let [modal1 (:modal1 (om/get-state this))
                                  modal-form (:modal-form (om/get-state this))]
                              (dom/div #js {:className "example-button"}
                                       (dom/h2 nil "Modal")
                                       (ant/button {:on-click #(om/update-state! this assoc :modal1 true)} "Modal Dialog")
                                       (ant/modal {:visible modal1 :title "Title of modal"
                                                   :on-ok #(om/update-state! this assoc :modal1 false) :on-cancel #(om/update-state! this assoc :modal1 false)} 
                                                  (dom/p nil "Some content 1"))
                                       (ant/button {:on-click #(ant/modal-confirm {:title "Are you sure?" :content "Some content"})} "Confirmation Modal")
                                       (ant/button {:on-click #(om/update-state! this assoc :modal-form true)} "Modal Form")
                                       (ant/modal {:visible modal-form :title "Modal Form" :width 600
                                                   :on-ok #(om/update-state! this assoc :modal-form false) :on-cancel #(om/update-state! this assoc :modal-form false)} 
                                                  (ant/create-form UserForm :props {:display-buttons? false}))))))))

(def form-example (om/factory
                   (om/ui
                    Object
                    (render [this]
                            (dom/div nil 
                                     (dom/h2 nil "Form")
                                     (ant/create-form UserForm :props {:display-buttons? true}))))))

(defn add-actions-column [columns comp]
  (conj columns 
    {:title "Actions"
     :render
        #(ant/button {:icon "delete" :type "danger"
                      :on-click
                        (fn []
                         (om/update-state! comp assoc :data
                           (remove (fn [d] (= (get (js->clj %2) "id") 
                                              (:id d))) (:data (om/get-state comp)))))})}))

(def datatable (om/factory
                (om/ui
                 Object
                 (componentWillMount [this]
                                        (om/set-state! this {:data common/people}))
                 (render [this]
                         (let [data (:data (om/get-state this))]
                           (dom/div nil
                                    (dom/h2 nil "Data Table")
                                    (ant/table
                                     {:columns (add-actions-column common/columns this) :dataSource data 
                                      :pagination common/pagination :row-key "id"
                                      :row-selection
                                      {:on-change
                                       #(let [selected (js->clj %2 :keywordize-keys true)]
                                          (ant/message-info (str "You have selected: " (map :name selected))))}})))))))

(defn render-example 
  "Render each example within a bordered box"
  [examples]
  ;; we need to generate a different key for each react element
  (ant/col {:span 12}
    (for [example examples]
      (dom/div #js {:className "box" :key (random-uuid)}
        (dom/div #js {:className "box-content"}
          (example))))))

(defn render-full-row
  [example]
  (ant/col {:span 24}
    (dom/div #js {:className "box" :key (random-uuid)}
      (dom/div #js {:className "box-content"}
        (example)))))

(defui ContentArea
  Object
  (render [this]
          (ant/layout-content {:class "content-area"}
                              (ant/row {:gutter 12}
                                       (render-example [carousel buttons messages timeline tree progress modal])
                                       (render-example [card tooltip notifications auto-complete localization avatar])
                                       )
                              (render-full-row form-example)
                              (render-full-row datatable)
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
                    (ant/menu-item (dom/span #js {:key "s"} (ant/icon {:type "home"}) "Menu Item"))
                    (ant/menu-sub-menu {:title (dom/span nil (ant/icon {:type "setting"}) "Sub Menu")}  
                                       (ant/menu-item (dom/span #js {:key "s"} (ant/icon {:type "user"}) "Item 1"))
                                       (ant/menu-item (dom/span #js {:key "s"} (ant/icon {:type "notification"}) "Item 2"))))))
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
