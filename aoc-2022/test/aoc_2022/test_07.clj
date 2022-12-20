(ns aoc-2022.test_07
  (:require [clojure.test :refer :all]
            [aoc-2022.07 :as src]))

(def test-case "$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k")

;; (testing "dir-tree-builder"
;;   (deftest ...
;;     (is (= (hash-map) {})))) ;; TOOD

(testing "directory-counter"
  (deftest example
    (is (= 95437 (src/sum-sizes-below-x 100000 (src/txt->tree test-case))))))
