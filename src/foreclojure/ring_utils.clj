(ns foreclojure.ring-utils
  (:require [foreclojure.config :as config]))

(def ^{:dynamic true} *url*         nil) ; url of current request
(def ^{:dynamic true} *host*        nil) ; Host header sent by client
(def ^{:dynamic true} *http-scheme* nil) ; keyword, :http or :https

(defn get-host [request]
  (get-in request [:headers "host"]))

(defn wrap-request-bindings [handler]
  (fn [req]
    (binding [*url* (:uri req)
              *host* (or (get-host req) "www.4clojure.com")
              *http-scheme* (:scheme req)]
      (handler req))))

(def static-url (if-let [host config/static-host]
                  #(str (name (or *http-scheme* :http)) "://" host "/" %)
                  #(str "/" %)))
