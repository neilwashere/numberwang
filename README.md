# numberwang

![wang](https://media2.giphy.com/media/Z9ldUiEOCtdeM/200_s.gif)

Given a number between 0 and 1000000 inclusive, return a grammatically correct english sentence.

    10 -> ten
    110 -> one hundred and ten
    1000 -> one thousand
    10001 -> ten thousand and one
    900023 -> nine hundred thousand and twenty-three

## Usage

Use from the command line

    lein run <number>

e.g.

    lein run 999
    > nine hundred and ninety nine

## Caveats

It uses an alpha release of clojure 1.9 which bundles `clojure.spec` as I wanted to try it out although I didn't get very far.

## Todo

* Add some generative tests!
* remove the entry point assert (it's really just for experimenting)
* What about I18N?????
* The `hundred-phrasing` func uses a number of qualitative approaches to determine the 'type' of number it is dealing with. This is asking for simplification.

## License

Copyright © 2016 neilwashere

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
