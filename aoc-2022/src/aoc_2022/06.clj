(ns aoc-2022.06)

(defn detect-packet-start
  "Given a string input, find the index at which the first sequence of 4, non-repeating characters ends."
  [packet]
  (->> packet
       (partition 4 1)
       (map (partial reduce conj #{}))
       (map count)
       (take-while (partial not= 4))
       (count)
       (+ 4)))

(defn -main
  "Performs the task described by Advent of Code day 6
   https://adventofcode.com/2022/day/6"
  []
  (detect-packet-start (slurp "resources/06.signal.txt")))
