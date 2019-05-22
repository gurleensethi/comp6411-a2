(defn read-file []
  (with-open [rdr (clojure.java.io/reader "map.txt")]
    (reduce (fn [arg1 arg2] 
              (conj arg1 arg2)) 
            [] (line-seq rdr))))

(defn split-strings 
  [strlist]
  (map #(clojure.string/split % #",") strlist))

(println (split-strings (read-file)))