(ns aoc-2022.test-06
  (:require [clojure.test :refer :all]
            [aoc-2022.06 :as src]))

(testing "packet-detector"
 (deftest examples
   (is (= 5 (src/detect-packet-start "bvwbjplbgvbhsrlpgdmjqwftvncz")))
   (is (= 6 (src/detect-packet-start "nppdvjthqldpwncqszvftbrmjlhg")))
   (is (= 10 (src/detect-packet-start "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
   (is (= 11 (src/detect-packet-start "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))))
