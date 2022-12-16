(ns aoc-2022.05
  (:require [clojure.string :as str]))

(defn pop-last-element
  "Given a vector v, pop the last element from the vector and return the resulting vector
   
   e.g.
   (pop-last-element [1 2 3])
   >>>
   [1 2]
   "
  [v]
  (subvec v 0 (- (count v) 1)))

(defn element-extractor
  "Given a stack number and the stacks in a textual format, return a fn that returns the nth element in that stack,
   where the 0th element is the top of the stack.
   
   e.g.
   
   ((element-extractor 0 stacks-txt) 1))
   >>>
   '[B]'
   
   N.B: Assumes total number of stacks is 9."
  [stack-number stacks-txt]
  (partial #(subs stacks-txt
                  ;; By assuming 9 stacks, and 4 chars per element (e.g. '[B] '), we get (* 9 4) >>> 36
                  (+ (* % 36) (* 4 stack-number))
                  (+ 3 (* % 36) (* 4 stack-number)))))

(defn stack-extractor
  "Given a stack number and the stacks in textual format, return a vector representing that stack.
   
   e.g.
   (stack-extractor 0 8 stacks-txt)
   >>>
   ['G' 'D' 'V' 'Z' 'J' 'S' 'B']"
  [stack-number stack-height stacks-txt]
  (->> (range stack-height)
       ((partial map (element-extractor stack-number stacks-txt)))
       ((partial map #(re-find #"[A-Z]" %)))
       ((partial filter some?))
       (reverse)
       (vec)))

(defn pop-crate
  "Given:
   - a vector of vectors (stacks)
   - a source stack index
   - a target stack index
   - a number of crates to move
   
   recursively call pop-create until all have been moved"
  [stacks source-idx target-idx n]
  (if (= n 0)
    stacks
    (let [[crate popped-source] ((juxt last pop-last-element) (stacks source-idx))
          pushed-target (vec (concat (stacks target-idx) [crate]))]  ;; cast to vec to avoid lazySeqs
      (pop-crate (-> stacks
                     (assoc source-idx popped-source)
                     (assoc target-idx pushed-target))
                 source-idx
                 target-idx
                 (dec n)))))

(defn create-stacks
  "Given a text input, e.g.
   
           [F] [Q]         [Q]        
   [B]     [Q] [V] [D]     [S]        
   [S] [P] [T] [R] [M]     [D]        
   [J] [V] [W] [M] [F]     [J]     [J]
   [Z] [G] [S] [W] [N] [D] [R]     [T]
   [V] [M] [B] [G] [S] [C] [T] [V] [S]
   [D] [S] [L] [J] [L] [G] [G] [F] [R]
   [G] [Z] [C] [H] [C] [R] [H] [P] [D]
   
   create a vector of vectors representing the stacks.
   e.g.
   
   [['G' 'D' 'V' 'Z' 'J' 'S' 'B']
    ['Z' 'S' 'M' 'G' 'V' 'P']
   ...]
   "
  [stacks-txt]
  (let [stack-height (count (str/split stacks-txt #"\n"))]
    (vec (map #(stack-extractor % stack-height stacks-txt) (range 9)))))  ;; 9 stacks are assumed

(defn string-step->step
  "Given a string definition of a step, return a hash-map of its salient information.
   This includes 0-indexing the source and target stacks.
   
   e.g.
   (string-step->step 'move 2 from 7 to 1')
   >>>
   {:n 2, :source-idx 6, :target-idx 0}
   "
  [string-step]
  (->> string-step
       (re-seq #"[0-9]{1,2}")
       (map #(Integer/parseInt %))
       (map vector [:n :source-idx :target-idx])
       (into {})
       (#(update % :source-idx dec))    ;; 0-indexing applied here...
       (#(update % :target-idx dec))))  ;; and here.

(defn txt->stacks+steps
  "Reads in a .txt file from a given filepath, returning a hash-map of stacks and steps, e.g.
  
  {:stacks [['G' 'D' 'V' 'Z' 'J' 'S' 'B']
            ['Z' 'S' 'M' 'G' 'V' 'P']
             ...]
   :steps [{:n 3, :source-idx 5, :target-idx 2}}
            ...]
   "
  [filepath]
  (let [[raw-stacks raw-steps] (str/split (slurp filepath) #"\n\n")]
    (hash-map :stacks (create-stacks (str/join "\n" (pop (str/split raw-stacks #"\n"))))
              :steps (vec (map string-step->step (str/split raw-steps #"\n"))))))  ;; cast to a vector as lazySeqs are causing problems for peeking and popping

(defn apply-all-steps
  "Given the stacks and the steps, apply all steps to the stacks sequentially"
  [steps stacks]
  (if (empty? steps)
    stacks
    (let [[step remaining-steps] ((juxt first #(subvec % 1)) steps)
          {source-idx :source-idx
           target-idx :target-idx
           n :n} step]
      (apply-all-steps remaining-steps (pop-crate stacks source-idx target-idx n)))))

(defn top-crates
  "Given the stacks, return the top crates on each stack in a single string"
  [stacks]
  (str/join "" (map last stacks)))

(defn -main
  "Performs the task described by Advent of Code day 5
   https://adventofcode.com/2022/day/5"
  []
  (let [{stacks :stacks
         steps :steps}
        (txt->stacks+steps "resources/05.supply-stacks.txt")]
    (top-crates (apply-all-steps steps stacks))))
