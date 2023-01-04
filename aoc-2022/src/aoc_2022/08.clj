(ns aoc-2022.08
  (:require [clojure.string :as str]
            [clojure.core.matrix :as m]))

(def test-case "30373
25512
65332
33549
35390")

(defn rolling-max
  "Given a 1D vector, v, returning the rolling max along that vector.
   
   e.g. (rolling-max [3 0 3 7 3])
   >>>
   [3 3 3 7 7]
   "
  [v]
  (loop [i 0
         max-so-far [0]]
    (if (< i (count v))
      (recur (inc i) (conj max-so-far
                           (if (> (v i) (last max-so-far))
                             (v i)
                             (last max-so-far))))
      (subvec max-so-far 1))))


(defn inverse-orientations
  "Given a hash-map containing an array's orientations
   
   {:row-major ...
    :column-major ...
    :reverse-row-major ...
    :reverse-column-major ...}
   
   Return a vector of those arrays all in row-major order."
  [orientations]
  (vector
   (:row-major orientations)
   (m/transpose (:column-major orientations))
   (m/array (map reverse (:reverse-row-major orientations)))
   (m/array (m/transpose (map reverse (:reverse-column-major orientations))))))


(defn array-orientations
  "Given a 2D array, return a hash-map containing the 4 directions in in which the array can be iterated.
  
   For any given (x, y) pair, there are 4 directions along which we can view the 2D array:
   - from the left
   - from the top
   - from the right
   - from the bottom
   
   [ ➡️ ➡️ ➡️
     . . .
     . . .
   ]

   [ ⬇ . .
     ⬇ . .
     ⬇ . .
   ]
   
   [ ⬅ ⬅ ⬅
     . . .
     . . .
   ]

   [ ⬆ . .
     ⬆ . .
     ⬆ . .
   ]
   
   We want to return an array that allows us to find the rolling max along each of these directions.
   To do that, we return a hash-map whose keys are the matrix in:

   - row-major order
   - column-major order
   - reverse-row-major order
   - reverse-column-major order
     
   https://en.wikipedia.org/wiki/Row-_and_column-major_order#/media/File:Row_and_column_major_order.svg
   "
  [array]
  (hash-map :row-major array,
            :column-major (m/transpose array)
            :reverse-row-major (m/array (map reverse array))
            :reverse-column-major (m/array (map reverse (m/transpose array)))))

(defn map-orientations
  "Given a hash-map of orientations
   
   orientations
   >>>
   {:row-major [[1 2 3] [4 5 6] [7 8 9]]
    :column-major ...
    :reverse-row-major ...
    :reverse-column-major ...}
   
   apply f to each vector, e.g.
   
   (map-orientations #(map inc %) orientations)
   >>>
   {:row-major [[2 3 4] [5 6 7] [8 9 10]]
    ...}
   "
  [f orientations]
  (reduce-kv (fn [m k v]
               (assoc m k (m/array (map f v)))) {} orientations))

(defn visible-trees
  "Given a 2D array of integers (representing trees), return a boolean array indicating whether or not a given tree is visible"
  [array]
  (let [orientations (array-orientations array)
        rolling-max-along-orientations (map-orientations rolling-max orientations)
        rolling-max-with-defaults (map-orientations #(assoc % 0 -1) rolling-max-along-orientations)  ;; We want all outer trees to be deemed 'visible' so set their height to -1.
        tree-heights (inverse-orientations rolling-max-with-defaults)]  
    (m/non-zero-count (reduce m/add (map #(m/gt array %) tree-heights)))))

(defn txt->array
  "Given a string representation of an array, e.g.
   
   '123
   456
   789'
   
   return an array data structure containing that data, e.g.
   
   [[1 2 3] [4 5 6] [7 8 9]]
   "
  [txt]
  (->> txt
       (#(str/split % #"\n"))
       (map #(str/split % #""))
       (map (partial map #(Integer/parseInt %)))
       (m/array)))

(defn -main
  "Performs the task described by Advent of Code day 8
   https://adventofcode.com/2022/day/8"
  []
  (-> "resources/08.trees.txt"
      (slurp)
      (txt->array)
      (visible-trees)))
