(def task-options
  {:project 'coconutpalm/boot-code
   :version "0.1.1"
   :project-name "boot-code"
   :project-openness :open-source

   :description "A programmers' editor intended to be embedded inside of build tools."

   :scm-url "https://github.com/coconutpalm/boot-code"

   :test-sources "test"
   :test-resources nil})


(defn qualify [path] (str (System/getProperty "user.dir") "/" path))


(set-env!
 :dependencies '[[org.clojure/clojure        "1.9.0"]

                 [coconutpalm/boot-boot     "LATEST"]
                 [clojure-watch             "LATEST"]
                 [adzerk/boot-cljs          "2.1.4"]
                 [adzerk/boot-reload        "0.6.0"]
                 [compojure                 "1.6.1"]
                 [hoplon/castra             "3.0.0-alpha7"]
                 [hoplon/hoplon             "7.2.0"]
                 [hoplon/javelin            "3.9.0"]

                 [clj-jgit                  "0.8.10"]
                 [eval-soup                 "1.4.3"]
                 [paren-soup                "2.12.3"]

                 [org.clojure/clojurescript "1.10.238"]
                 [ring/ring-defaults        "0.3.2"]]


 :resource-paths #{(qualify "resources") (qualify "src/clj")}
 :source-paths   #{(qualify "src/cljs") (qualify "src/hl")})


(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-reload    :refer [reload]]
  '[hoplon.boot-hoplon    :refer [hoplon prerender]]
  '[pandeiro.boot-http    :refer [serve]])


(deftask web-dev
  "Build boot-code for local development."
  []
  (comp (serve
      :port    7000
      :handler 'boot-code.handler/app
      :reload  true)
     (watch)
     (speak)
     (hoplon)
     (reload)
     (cljs)))


#_(deftask prod
  "Build boot-code for production deployment."
  []
  (comp (hoplon)
     (cljs :optimizations :advanced)))


#_(deftask make-war
  "Build a war for deployment"
  []
  (comp (hoplon)
     (cljs :optimizations :advanced)
     (uber :as-jars true)
     (web :serve 'sbt_hoplon.handler/app)
     (war)
     (target :dir #{"target"})))
