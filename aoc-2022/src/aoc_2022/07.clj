(ns aoc-2022.07
  (:require [clojure.string :as str]))

(defn sum-files
  "Given a dir, e.g.
   
   {:files ('14848514 b.txt' '8504156 c.dat')
    :dirs ('a' 'd')}
   
   sum the size of the files"
  [dir]
  (reduce + (map (comp #(Integer/parseInt %) #(re-find #"[0-9]{1,}" %)) (:files dir))))

(defn directory-size
  "Given a directory tree, tree, and a target dir, dir, return its size"
  ;; There's presumably a more efficient way to do this, as this method will recompute values for nested dirs.
  [tree dir]
  (reduce +
          (conj (map (partial directory-size tree) (get-in tree [dir :dirs]))
                (sum-files (tree dir)))))

(defn sum-sizes-below-x
  "Given a threshold x, return the sum of all dir sizes whose size is <= x"
  ([tree]
   (sum-sizes-below-x 100000 tree))
  ([threshold tree]
   (->> tree
        (#(map (partial directory-size %) (keys %)))
        (filter #(<= % threshold))
        (reduce +))))

(defn dir-contents->files+dirs
  "Given a str representing a directory contents. e.g.

   '$ ls\ndir a\n14848514 b.txt\n8504156 c.dat\ndir d\n'

   return a hash-map of files and dirs in that dir, e.g.
   {:files ('14848514 b.txt' '8504156 c.dat')
    :dirs ('a' 'd')}
   "
  [dir-contents-raw]
  (let [files (re-seq #"[0-9]{1,}\s[a-z].*" dir-contents-raw)
        subdirs  ((comp (partial map #(if (nil? %) % (subs % 4))) #(re-seq #"dir\s[a-z].*" %)) dir-contents-raw)]
    (hash-map :files files, :dirs subdirs)))

(defn txt->tree
  "Given a txt input, return a tree representing the directory structure, e.g.
   
   {'/' {:dirs ('a' 'd'),
         :files ('14848514 b.txt' '8504156 c.dat')},
    'a' {:dirs ('e'), 
         :files ('29116 f' '2557 g' '62596 h.lst')},
    ...}"
  [txt]
  (let [pattern #"\$\scd\s([a-z].*|\/)\n"
        dirs-names (map last (re-seq pattern txt))
        dirs-contents-raw (subvec (str/split txt pattern) 1)
        dirs-contents (map dir-contents->files+dirs dirs-contents-raw)]
    (reduce conj (zipmap dirs-names dirs-contents) {})))

(defn -main
  "Performs the task described by Advent of Code day 7
   https://adventofcode.com/2022/day/7"
  []
  (->>  "resources/07.terminal.txt"
        (slurp)
        (txt->tree)
        (sum-sizes-below-x 100000)))
