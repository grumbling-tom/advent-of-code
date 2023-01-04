(ns aoc-2022.09
  (:require [clojure.string :as str]))

;; Rope has knot at either end - the head and tail of a rope
;; If the head moves far from the tail, the tail is pulled to the head

;; Model knot positions on a 2D grid.
;; Heads and tails must be touching
;; Touching means adjacent, diagonal or overlapping.

;; If the head is two steps directly up, down, left or right, the tail moves one step to the head in the appropriate directiom.
;; If the head and tail are not touching and are not in the same row or column, the tail moves one diagonal step to keep up

;; The head and tail start in an overlapping position.
;; You must figure out where the tail goes in response to the head moving.

;; Count the number of squares visited by the tail.

;; 6x5 grid.
;; Can we assume no out of bounds exceptions will be raised?

(def test-case "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2")

(def init-position {:x 0
                    :y 0})

(def move {:axis :x
           :step -2})

(def visited-positions #{init-position})

(defn perform-move
  "Given an (x, y) coordinate for the head and tail,
   move the head by the specified move.
   Move the tail correspondingly.
   
   Returns the new coordinates of the head and tail.
   
   e.g.
   (perform-move ...)
   >>>
   ..."
  [head-coord tail-coord move]
  ())

(defn perform-moves
  "..."
  [visited-positions])

(defn txt->move
  "Given a text input represnting a move, convert that to a hash-map representation.
   
   e.g.
   
   (txt->move 'U 4')
   >>>
   {:axis :y
    :step 4}"
  [txt]
  (let [[direction step-str] (str/split txt #" ")
        step (#(Integer/parseInt %) step-str)]
    (hash-map :axis (case direction
                      "U" :y
                      "D" :y
                      "L" :x
                      "R" :x),
              :step (if (or (= "U" direction) (= "R" direction))
                      step
                      (* -1 step)))))

(defn -main
  "Performs the task described by Advent of Code day 9
   https://adventofcode.com/2022/day/9"
  []
  (->> "resources/09.rope-bridge.txt"
      (slurp)
      (#(str/split % #"\n"))
      (map txt->move)))
