(ns fipp.benchmark
  (:require [clojure.pprint]
            [threatgrid.fipp.edn]
            [threatgrid.fipp.clojure]
            [criterium.core :refer [bench]]))

(def benched-fns [
  ;prn
  threatgrid.fipp.edn/pprint
  threatgrid.fipp.clojure/pprint
  ;clojure.pprint/pprint
])

(def samples {
  :long (vec (range 10000))
  ;; Dirty hacky source of test data:
  :mixed (read-string {:read-cond :allow} (str "[" (slurp "src/threatgrid/fipp/engine.cljc") "]"))
})

;; lein run -m fipp.benchmark
(defn -main []
  (doseq [f benched-fns
          [k v] samples]
    (println "Testing " f " on " k)
    (criterium.core/with-progress-reporting
      (bench (with-out-str (f v))))))
