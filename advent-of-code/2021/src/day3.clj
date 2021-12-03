(def path "d:/dev/ps/advent-of-code/2021/input/day3.txt")

(defn merge-bit-pair [a b]
  (merge-with + a b))

(defn merge-bits [as bs]
  (map merge-bit-pair as bs))

(defn bit-freq-per-position [s]
  (->> s
       (map (fn [line] (map (fn [c] {c 1}) line)))
       (reduce merge-bits)))

(merge-bit-pair {:a 1} {:b 1 :a 2})
(merge-bits [{:a 1} {:b 1 :a 2}] [{:a 1} {:b 1 :a 2}])

(defn bin [bs]
  (read-string (str "2r" bs)))

(bin "0")
(bin "10")
(bin "101")

(defn gamma-rate [lines]
  (->> lines
       bit-freq-per-position
       (map (fn [bit] (key (apply max-key val bit))))
       (clojure.string/join)
       (bin)))

(defn eps-rate [lines]
  (->> lines
       bit-freq-per-position
       (map (fn [bit] (key (apply min-key val bit))))
       (clojure.string/join)
       (bin)))

(def input-lines
  (with-open [r (clojure.java.io/reader path)]
    (vec (line-seq r))))

; part 1
(* (gamma-rate input-lines) (eps-rate input-lines))

(defn part-2 [lines bit-criteria pos]
  (if (== (count lines) 1)
    (bin (first lines))
    (let [keep-bit (bit-criteria (nth (bit-freq-per-position lines) pos))
          filtered (filter (fn [line] (= (nth line pos) keep-bit)) lines)]
      (part-2 filtered bit-criteria (+ pos 1)))))

(defn o2-rating [lines]
  (let [most-common-bit (fn [bit] (if (<= (bit \0) (bit \1)) \1 \0))]
    (part-2 lines most-common-bit 0)))
(defn co2-rating [lines]
  (let [least-common-bit (fn [bit] (if (<= (bit \0) (bit \1)) \0 \1))]
    (part-2 lines least-common-bit 0)))

; part 2
(* (o2-rating input-lines) (co2-rating input-lines))