(defn read-file []
  (with-open [rdr (clojure.java.io/reader "map.txt")]
    (reduce (fn [arg1 arg2]
              (conj arg1 arg2))
            [] (line-seq rdr))))

(defn split-strings
  [strlist]
  (vec (map #(clojure.string/split % #"") strlist)))

(defn get-xy [x y strlist] (nth (nth strlist y) x))

(defn is-visited [value strlist] (if (get strlist value) true false))

(defn is-safe-move
  [x y strlist]
  (if (and (< x (count (first strlist))) (>= x 0))
    (if (and (< y (count strlist)) (>= y 0))
      (and (not= (get-xy x y strlist) "#") (not= (get-xy x y strlist) "!"))
      false)
    false))

(defn mark-visited
  [x y value strlist]
  (if (= (get-xy x y (get strlist :map)) "@")
    (assoc strlist :found true)
    (assoc strlist :map
           (assoc (get strlist :map) y (assoc (nth (get strlist :map) y) x value)))))

(defn map-move-to-num
  [key move]
  (get (get {:x {:left -1 :right 1} :y {:up -1 :down 1}} key {}) move 0))

(defn pretty-print
  [strlist]
  (doseq [i strlist]
    (println (clojure.string/join "" i))))

(defn find-path
  [rows cols strlist visited x y]

  (def maze (reduce (fn [maze move]
                      (if (get maze :found)
                        (do
                          (mark-visited x y "+" maze))
                        (if (= (get-xy x y (get maze :map)) "@")
                          (assoc maze :found true)
                          (do
                            (if (is-safe-move (+ x (map-move-to-num :x move)) (+ y (map-move-to-num :y move)) (get maze :map))
                              (let [result-maze (find-path rows cols (mark-visited x y "!" maze) visited (+ x (map-move-to-num :x move)) (+ y (map-move-to-num :y move)))]                                
                                (if (get result-maze :found)
                                  (mark-visited x y "+" result-maze)
                                  result-maze))
                              (if (get maze :found)
                                (mark-visited x y "+" maze)
                                (mark-visited x y "!" maze)))))))
                    strlist [:up :down :left :right]))
  maze)

(def moves {:x {:left -1 :right 1} :y {:up -1 :down 1}})
(def original-treasure-map (split-strings (read-file)))
(def treasure-map (split-strings (read-file)))
(def rows (count treasure-map))
(def cols (count (first treasure-map)))

(do
  (println "This is my challenge:\n")
  (pretty-print treasure-map)
  (println "\n\n")
  (let [solved-maze (find-path rows cols {:map treasure-map :found false} #{} 0 0)]
    (if (:found solved-maze)
      (println "Woo hoo, I found the treasure :-)\n")
      (println "Uh oh, I could not find the treasure :-(\n"))
    (pretty-print (:map solved-maze))))