(ns numberwang.core
  (:require [clojure.spec :as s]))

(s/def ::input-boundary (s/int-in 0 2001))
(s/def ::input-text (s/and string? #(re-matches #"\d+" %)))

(defn exponent-tag
  [num]
  (condp #(>= %2 %1) num
    1000 " thousand"
    0 ""))

(def number-word-map
  {:natural {0  ""
             1  "one"
             2  "two"
             3  "three"
             4  "four"
             5  "five"
             6  "six"
             7  "seven"
             8  "eight"
             9  "nine"
             10 "ten"
             11 "eleven"
             12 "twelve"
             13 "thirteen"
             14 "fourteen"
             15 "fifteen"
             16 "sixteen"
             17 "seventeen"
             18 "eighteen"
             19 "ninteen"}
   :tens {2 "twenty"
          3 "thirty"
          4 "fourty"
          5 "fifty"
          6 "sixty"
          7 "seventy"
          8 "eighty"
          9 "ninety"}})


(defn wordify
  ([units] (wordify units 0 0))
  ([units tens] (wordify units tens 0))
  ([units tens hundreds]
   (let [base10       (+ units (* 10 tens))
         tens-word    (if (<= base10 19 )
                        (get (:natural number-word-map) base10)
                        (format "%s %s" (get (:tens number-word-map) tens)
                                (get (:natural number-word-map) units)))
         hundred-word (when (> hundreds 0)
                        (format "%s hundred" (get (:natural number-word-map) hundreds)))
         builder (remove nil? [hundred-word
                               (when (and hundred-word (> (count tens-word) 0))
                                 "and")
                               tens-word])]
     (clojure.string/join " " builder))))

(defn number-word-walk
  [n]
  (loop [result []
         base 100
         num (reverse n)]
    (if (empty? num)
      result
      (let [dig-tup   (map read-string (map str (take 3 num)))
            worded    (apply wordify dig-tup)
            qualifier (exponent-tag base)]
        (recur (cons (str worded qualifier) result)
               (* base 10)
               (drop 3 num))))))

(defn -main
  [^String num]
  {:pre [(s/valid? ::input-text num)
         (s/valid? ::input-boundary (read-string num))]}
  (println (clojure.string/join " " (number-word-walk num))))
