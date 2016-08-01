(ns numberwang.core
  (:require [clojure.spec :as s]
            [clojure.string :as string]))

(s/check-asserts true)

(s/def ::input-boundary (s/int-in 0 20001))
(s/def ::input-text (s/and string? #(re-matches #"\d+" %)))
(s/def ::positive-int (s/and int? #(>= % 0)))
(s/def ::digit (s/int-in 0 10))

(defn- exponent-tag
  [num]
  (condp #(>= %2 %1) num
    1000 " thousand"
    0 ""))

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

(s/fdef num->phrase
  :args (s/cat :units ::digit :tens ::digit :hundreds ::digit)
  :ret string?
  :fn #(let [base10 (+ (-> % :args :units) (* 10 (-> % :args :units)))]
         (when (and (<= base10 19) (> base10 0))
           (string/includes? (:ret %) (get (:natural number-word-map) base10)))
         (when (and (= base10 0) (> (-> & :args :hundreds) 0))
           (re-matches #".* hundred$" (:ret %)))
         (when (and (> base10 0) (> (-> % :args :hundreds) 0))
           (re-matches #".* hundred and .*" (:ret %)))))

(defn- num->phrase
  ([units] (num->phrase units 0 0))
  ([units tens] (num->phrase units tens 0))
  ([units tens hundreds]
   (let [base10        (+ units (* 10 tens))

         tens-word     (if (<= base10 19 )
                         (get (:natural number-word-map) base10)
                         (str (get (:tens number-word-map) tens)
                              " "
                              (get (:natural number-word-map) units)))

         hundred-word  (when (> hundreds 0)
                         (format "%s hundred" (get (:natural number-word-map) hundreds)))

         and-word      (when (and hundred-word (> (count tens-word) 0))
                         "and")

         number-phrase (remove nil? [hundred-word and-word tens-word])]
     (string/trimr (string/join " " number-phrase)))))


(defn number-word-walk
  [n]
  {:pre [(s/valid? ::positive-int n)]}
  (if (= 0 n)
    "zero"
    (loop [result []
           base   100
           num    (reverse (str n))]
      (if (empty? num)
        (string/trimr (string/join " " result))
        (let [dig-tup   (map read-string (map str (take 3 num)))
              worded    (apply num->phrase dig-tup)
              qualifier (exponent-tag base)]
          (recur (cons (str worded qualifier) result)
                 (* base 10)
                 (drop 3 num)))))))

(defn -main
  [num]
  {:pre [(s/valid? ::input-text num)]}
  (let [n (s/assert ::input-boundary (read-string num))]
    (println (number-word-walk n))))
