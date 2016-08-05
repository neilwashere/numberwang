(ns numberwang.core
  (:require [clojure.spec :as s]
            [clojure.string :as string]))

(s/check-asserts true)

(s/def ::input-boundary (s/int-in 0 1000001))
(s/def ::input-text (s/and string? #(re-matches #"\d+" %)))
(s/def ::positive-int (s/and int? #(>= % 0)))

(defn- exponent-tag
  [num]
  (condp #(>= %2 %1) num
    100000000 "million"
    100000 "thousand"
    100 nil))

(def ^:private number-word-map
  {:natural {1  "one"
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

(defn- hundred-phrasing
  [{:keys [units tens hundreds]} base number]
  (let [base10       (+ units (* 10 tens))
        tens-word    (if (<= base10 19 )
                       (get (:natural number-word-map) base10)
                       (let [ten-word  (get (:tens number-word-map) tens)
                             unit-word (get (:natural number-word-map) units)
                             hyphen    (when (and ten-word unit-word) "-")]
                         (str ten-word hyphen unit-word)))
        hundred-word (when (> hundreds 0)
                       (format "%s hundred" (get (:natural number-word-map) hundreds)))
        and-word     (when (and hundred-word (> (count tens-word) 0))
                       "and")
        grammar-and  (when (and (= 100 base)
                                (>= number 1001)
                                (= 0 hundreds))
                       "and")
        exponent     (when (> (+ units tens hundreds) 0)
                       (exponent-tag base))
        phrase       (remove nil? [grammar-and
                                   hundred-word
                                   and-word
                                   tens-word
                                   exponent])]
    (when (seq phrase) (string/join " " phrase))))


(defn- sentence-phrasing
  [m]
  (let [phrasing (reduce (fn [phrase-array k]
                           (cons (hundred-phrasing (get m k) k (:number m))
                                 phrase-array))
                         []
                         (sort (filter int? (keys m))))]
    (string/join " " (remove nil? phrasing))))

(defn- number-tupple-map
  [units & [tens hundreds]]
  {:units    (or units 0)
   :tens     (or tens 0)
   :hundreds (or hundreds 0)})

(defn- number-analysis
  [n]
  (loop [analysis {:number n}
         base 100
         num (reverse (str n))]
    (if (empty? num)
      analysis
      (let [digit-tupple (map read-string (map str (take 3 num)))
            tupple-map (apply number-tupple-map digit-tupple)]
        (recur (assoc analysis base tupple-map)
               (* base 1000)
               (drop 3 num))))))

(defn number->phrase
  [n]
  (if (= n 0)
    "zero"
    (-> n
        number-analysis
        sentence-phrasing)))

(defn -main
  [num]
  {:pre [(s/valid? ::input-text num)]}
  (let [n (s/assert ::input-boundary (read-string num))]
    (println (number->phrase n))))
