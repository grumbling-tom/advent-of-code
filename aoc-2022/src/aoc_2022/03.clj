(ns aoc-2022.03
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn generate-priorties
  "Generates the priority score assigned to each item in a rucksack as a hash-map, e.g.
   
   {'a' 1,
    'b' 2,
    ...}"
  []
  (let [lowercase-alphabet (map (comp str char) (range 97 123))
        both-alphabets (concat lowercase-alphabet (map str/upper-case lowercase-alphabet))]
    (zipmap both-alphabets (range 1 (+ 1 (count both-alphabets))))))

(defn halve-string
  "Given a string of even length, return a vec of two strings, e.g.
   
   ['first-half' 'second-half']"
  [string-to-halve]
  (let [length (count string-to-halve)]
    ((juxt #(subs % 0 (/ length 2)) #(subs % (/ length 2))) string-to-halve)))

(defn txt->rucksacks
  "Reads in a .txt file from a given filepath, return a seq of 2-element vecs, e.g.
   
   (['lzczcWwwznGhB' 'gPSvTlCrNgqNC']
    ['DJVDVdvpmZd' 'PgrCbgbgCJC']
    ...)
   "
  [filepath]
  (-> filepath
      (slurp)
      (str/split #"\n")
      ((partial map halve-string))))

(defn find-shared-item
  "Given a vec of 2 strings, find the char [a-zA-Z] that appears in both"
  [sacks]
  (first (reduce set/intersection (map (comp set #(str/split % #"")) sacks))))

(defn -main
  "Performs the task described by Advent of Code day 2
   https://adventofcode.com/2022/day/3"
  []
  (let [priorities (generate-priorties)
        shared-items (map find-shared-item (txt->rucksacks "resources/03.rucksacks.txt"))]
    (->> shared-items
         (map priorities)
         (reduce +))))
