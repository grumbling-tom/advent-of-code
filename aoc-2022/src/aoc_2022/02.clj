(ns aoc-2022.02
  (:require [clojure.string :as str]))

(def move-lookup {:A :rock
                  :B :paper
                  :C :scissors
                  :X :rock
                  :Y :paper
                  :Z :scissors})

(def points-by-move {:rock 1
                     :paper 2
                     :scissors 3})

(def points-by-result {:win 6
                       :draw 3
                       :loss 0})

(defn determine-result
  "Given the opponent's move and your own, return :win :draw or :loss, as appropriate"
  [opponent player]
  (let [opponent (move-lookup opponent)
        player (move-lookup player)]
    (if (= opponent player)
      :draw
      (case opponent
        :rock (if (= player :paper) :win :loss)
        :paper (if (= player :scissors) :win :loss)
        :scissors (if (= player :rock) :win :loss)))))

(defn score
  "Given the opponent's move and your own, return your score in that round"
  [opponent player]
  (+ (points-by-result (determine-result opponent player))
     (points-by-move (move-lookup player))))

(defn total-score
  "Given a seq of 2-element seqs, e.g.
   
   ((:A :Y)
    (:B :X)
    ...)
   
   return the total score from those games"
  [rounds]
  (reduce + (for [[opponent player] rounds]
              (score opponent player))))

(defn txt->rounds
  "Reads in a .txt file from a given filepath, return a seq of 2-element seqs, e.g.
   
   ((:A :Y)
    (:B :X)
    ...)
   "
  [filepath]
  (-> filepath
      (slurp)
      (str/split #"\n")
      ((partial map #(str/split % #" ")))
      ((partial map #(map keyword %)))))

(defn -main
  "Performs the task described by Advent of Code day 2
   https://adventofcode.com/2022/day/2"
  []
  ;; This could have instead been implemented by:
  ;; - calculating the points for a given result (e.g. {(:A :Y) 8, ...})
  ;; - calling (frequencies (txt->rounds ...))
  ;; - joining on the key
  ;; - and finally multiplying.
  (-> "resources/02.rock-paper-scissors.txt"
      (txt->rounds)
      (total-score)))
