(ns numberwang.core-test
  (:require [clojure.test :refer :all]
            [numberwang.core :as sut]))

;; TODO - some generative testing
(deftest number-word-walk
  (testing "given a number number, it will produce hundred-base sentences"
    (is (= "one thousand" (sut/number-word-walk 1000)))
    (is (= "one hundred" (sut/number-word-walk 100)))
    (is (= "one hundred and sixteen" (sut/number-word-walk 116)))
    (is (= "five hundred and twenty three" (sut/number-word-walk 523)))
    (is (= "twenty" (sut/number-word-walk 20)))
    (is (= "zero" (sut/number-word-walk 0)))))

(deftest -main
  (testing "It will throw an error if the input is not a string of digits"
    (is (thrown? AssertionError (sut/-main "221hello"))))
  (testing "It will thrown an error if the input is out of range"
    (is (thrown? clojure.lang.ExceptionInfo (sut/-main "1001")))))
