(ns cwchriswilliams.analyzers.analyzers)

(defn build-analyzer-definition
  [f analyzer-type]
  {::analyzer-type analyzer-type ::fn f})

(defn build-raw-text-analyzer-definition
  [f]
  (build-analyzer-definition f ::raw-text))


(defn build-forms-analyzer-definition
  [f]
  (build-analyzer-definition f ::forms))

(defn is-analyzer-of-type?
  [analyzer analyzer-type]
  (= analyzer-type (::analyzer-type analyzer)))

(defn is-raw-text-analyzer?
  [analyzer]
  (is-analyzer-of-type? analyzer ::raw-text))

(defn is-forms-analyzer?
  [analyzer]
  (is-analyzer-of-type? analyzer ::forms))

(defn get-fn
  [analyzer]
  (::fn analyzer))


(defn get-raw-text-analyzers [analyzers] (map get-fn (filter is-raw-text-analyzer? analyzers)))

(defn get-forms-analyzers [analyzers] (map get-fn (filter is-forms-analyzer? analyzers)))


(defn perform-raw-text-analysis
  [analyzers raw-text]
  (reduce merge {} (map #(% raw-text) (get-raw-text-analyzers analyzers))))

(defn perform-form-analysis
  [analyzers forms]
  (reduce merge {} (map #(% forms) (get-forms-analyzers analyzers))))