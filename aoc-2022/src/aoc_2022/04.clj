(ns aoc-2022.04
  (:require [clojure.string :as str]))

(defn range->ints
  "Given a string range, e.g. 20-45, return a seq of ints"
  [string-range]
  (map #(Integer/parseInt %) (re-seq #"[0-9]{1,2}" string-range)))

(defn contained?
  "Given two seqs a and b, each containing two ints, return true if either is entirely contained by the other, else false"
  [a b]
  (let [[min-a max-a] a
        [min-b max-b] b]
    (or
     (and (>= min-a min-b) (<= max-a max-b))
     (and (>= min-b min-a) (<= max-b max-a)))))

(defn txt->section-ranges
  "Reads in a .txt file from a given filepath, return a seq of 2-element vecs, e.g.
   
   (['1-88' '88-92']
    ['46-47' '46-51']
    ...)
   "
  [filepath]
  (-> filepath
      (slurp)
      (str/split #"\n")
      ((partial map #(str/split % #",")))))

(defn -main
  "Performs the task described by Advent of Code day 4
   https://adventofcode.com/2022/day/4"
  []
  (let [string-ranges (txt->section-ranges "resources/04.camp-sections.txt")
        integer-ranges (map #(map range->ints %) string-ranges)]
    ((frequencies  ;; count true values
      (for [[a b] integer-ranges]  ;; destructure integer ranges
        (contained? a b))) true)))
