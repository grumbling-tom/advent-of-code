(ns aoc-2022.01
  (:require [clojure.string :as str]))

(defn strings->ints
  "Parses a seq of strings as ints"
  [strs]
  (map #(Integer/parseInt %) strs))

(defn txt->calories
  "Reads in a .txt file from a given filepath, return a seq of seqs, e.g.
   
   (['2000' '5489' ...]
    ['6693' '3867' ...]
    ...)
   "
  [filepath]
  (-> filepath
      (slurp)
      (str/split #"\n\n")
      ((partial map #(str/split % #"\n")))
      ((partial map strings->ints))))

(defn max-calories
  "Given a seq of of seq of ints, find the seq with the largest sum"
  [calories]
  (->> calories
       (map #(reduce + %))
       (apply max)))

(defn -main
  "Performs the task described by Advent of Code day 1
   https://adventofcode.com/2022/day/1"
  []
  (max-calories (txt->calories "resources/01.calories.txt")))
