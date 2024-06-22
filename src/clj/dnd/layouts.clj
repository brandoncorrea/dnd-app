(ns dnd.layouts
  (:require [c3kit.apron.utilc :as utilc]
            [c3kit.wire.api :as api]
            [c3kit.wire.assets :refer [add-fingerprint]]
            [c3kit.wire.flash :as flash]
            [c3kit.wire.jwt :as jwt]
            [clj-stacktrace.core :as cst]
            [clj-stacktrace.repl :as cstr]
            [clojure.string :as str]
            [dnd.config :as config]
            [hiccup.core :as hiccup]
            [hiccup.element :as elem]
            [hiccup.page :as page]
            [ring.util.response :as response]))

(defn default
  ([body] (default body nil))
  ([body head]
   (-> (response/response
         (page/html5
           [:head
            [:title "D&D"]
            [:meta {:charset "utf-8"}]
            [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, minimum-scale=1.0"}]
            [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"}]
            head
            (page/include-css (add-fingerprint "/css/dnd.css"))]
           [:body body]))
       (response/content-type "text/html")
       (response/charset "UTF-8"))))

(defn not-found []
  (-> (default [:h1 "404 - Not Found"])
      (response/status 404)))

(defn- ->main-script [data]
  (let [payload (pr-str (utilc/->transit data))]
    (str "<script type=\"text/javascript\">\n//<![CDATA[\n"
         "dnd.main.main(" (str/replace payload "</script>" "<\\/script>") ");"
         "\n//]]>\n</script>")))

(defn build-rich-client-payload [request]
  {:flash  (flash/messages request)
   :config {
            :anti-forgery-token (jwt/client-id request)
            :ws-csrf-token      (jwt/client-id request)
            :api-version        (api/version)
            :environment        config/environment
            :host               config/host
            }})

(defn- ->rich-head []
  (list
    (when config/development? (page/include-js "/cljs/goog/base.js"))
    (page/include-js (add-fingerprint "/cljs/dnd.js"))
    (elem/javascript-tag (str "goog.require('dnd.main');"))))

(defn- ->rich-body [request]
  (list [:div#app-root]
        (->main-script (build-rich-client-payload request))))

(defn web-rich-client
  "Load the default web page and let the client side take over."
  [request]
  (default (->rich-body request)
           (->rich-head)))

(defn- elem-partial [elem]
  (if (:clojure elem)
    [:tr.clojure
     [:td.source (hiccup/h (cstr/source-str elem))]
     [:td.method (hiccup/h (cstr/clojure-method-str elem))]]
    [:tr.java
     [:td.source (hiccup/h (cstr/source-str elem))]
     [:td.method (hiccup/h (cstr/java-method-str elem))]]))

(defn error [ex]
  (let [[ex & causes] (iterate :cause (cst/parse-exception ex))]
    (default
      [:div.content
       [:section.bad-gateway.hero]
       [:section.message
        [:h1 "Oh no!"]
        [:h4 "Something went wrong. Please try your request again."]]
       [:div.exception
        [:h4 (hiccup/h (.getName ^Class (:class ex)))]
        [:div.message (hiccup/h (:message ex))]
        [:div.trace
         [:table
          [:tbody (map elem-partial (:trace-elems ex))]]]
        (for [cause causes :while cause]
          [:div.causes
           [:h2 "Caused by " [:span.class (hiccup/h (.getName ^Class (:class cause)))]]
           [:div.message (hiccup/h (:message cause))]
           [:div.trace
            [:table
             [:tbody (map elem-partial (:trace-elems cause))]]]])]])))
