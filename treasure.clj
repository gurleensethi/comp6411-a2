(defn read-file []
  (with-open [rdr (clojure.java.io/reader "test.txt")]
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
  (println (str "Found: " (get strlist :found)))
  (doseq [i (get strlist :map)]
    (println i)))

(defn find-path
  [rows cols strlist visited x y]

  (if (= (get-xy x y (get strlist :map)) "@")
    (pretty-print (get strlist :map)))
  (println (str "## at => " (get-xy x y (get strlist :map)) "; x -> " x "; y -> " y))
  (pretty-print strlist)

  (def maze (reduce (fn [maze move]
                      (println (str "Currently at => " (get-xy x y (get maze :map)) "; x -> " x "; y -> " y))
                      (if (get maze :found)
                        (do 
                          (mark-visited x y "+" maze)
                          )
                        (if (= (get-xy x y (get maze :map)) "@")
                          (assoc maze :found true)
                          (do
                            (if (is-safe-move (+ x (map-move-to-num :x move)) (+ y (map-move-to-num :y move)) (get maze :map))
                              (let [result-maze (find-path rows cols (mark-visited x y "!" maze) visited (+ x (map-move-to-num :x move)) (+ y (map-move-to-num :y move)))]
                                (if (get result-maze :found)
                                  (println "We are here !!!!"))
                                (if (get result-maze :found)
                                  (mark-visited x y "+" result-maze)
                                  result-maze
                                  ))
                              (if (get maze :found)
                                (mark-visited x y "+" maze)
                                (mark-visited x y "!" maze)))
                            )))
                      )
                    strlist [:up :down :left :right]))
  (println "\nReturned maze")
  (pretty-print maze)
  (println "Returned maze\n")
  maze)

(def moves {:x {:left -1 :right 1} :y {:up -1 :down 1}})
(def original-treasure-map (split-strings (read-file)))
(def treasure-map (split-strings (read-file)))
(def rows (count treasure-map))
(def cols (count (first treasure-map)))

(println rows)
(println cols)
(find-path rows cols {:map treasure-map :found false} #{} 0 0)

; (defn recursive-backtracker
;   [current-x current-y grid]
;   (reduce (fn [grid direction]
;             (let [next-x (+ current-x (DX direction))
;                   next-y (+ current-y (DY direction))]
;               (if (valid-unvisited-cell? next-x next-y grid)
;                 (recursive-backtracker next-x next-y
;                                        (remove-walls current-x current-y next-x next-y
;                                                      direction grid))
;                 grid)))
;           grid, (clojure.core/shuffle [:N :S :E :W])))


; (defn find-path
;   [rows cols strlist visited x y]
;   (if (= (get-xy x y strlist) "@")
;     (println "Found solution now print it!")
;     (if (and (not (is-visited (+ x 1) y)) (is-safe-move (+ x 1) y strlist))
;       (find-path rows cols strlist visited (+ 1 x) y)
;       (if (and (not (is-visited x (+ y 1))) (is-safe-move x (+ y 1) strlist))
;         (find-path rows cols strlist visited x (+ 1 y)))
;       )
;     )
;   (println (str "Currently at => " (get-xy x y strlist) "; x -> " x "; y -> " y))
;   )
