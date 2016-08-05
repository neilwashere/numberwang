(ns numberwang.core-test
  (:require [clojure.test :refer :all]
            [numberwang.core :as sut]))

;; TODO - some generative testing
(deftest number->phrase
  (testing "given a number number, it will produce hundred-base sentences"
    (is (= "one thousand" (sut/number->phrase 1000)))
    (is (= "one hundred" (sut/number->phrase 100)))
    (is (= "one hundred and sixteen" (sut/number->phrase 116)))
    (is (= "five hundred and twenty-three" (sut/number->phrase 523)))
    (is (= "twenty" (sut/number->phrase 20)))
    (is (= "zero" (sut/number->phrase 0)))))

(deftest number-exponent
  (testing "when a number is greater than 1000"
    (is (= "one thousand and one" (sut/number->phrase 1001)))
    (is (= "one million and one" (sut/number->phrase 1000001)))))

(deftest -main
  (testing "It will throw an error if the input is not a string of digits"
    (is (thrown? AssertionError (sut/-main "221hello"))))
  (testing "It will thrown an error if the input is out of range"
    (is (thrown? clojure.lang.ExceptionInfo (sut/-main "1000001")))))
