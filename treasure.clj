(defn read-file []
  (with-open [rdr (clojure.java.io/reader "map.txt")]
    (reduce (fn [arg1 arg2] 
              (conj arg1 arg2)) 
            [] (line-seq rdr))))

(println (map #(str/split % "") (read-file)))