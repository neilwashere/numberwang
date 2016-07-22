# numberwang

![wang](https://media2.giphy.com/media/Z9ldUiEOCtdeM/200_s.gif)

Given a number between 0 and 1000 inclusive, return a grammatically correct english sentence.

    10 -> ten
    110 -> one hundred and ten
    1000 -> one thousand

## Usage

Use from the command line

    lein run <number>

e.g.

    lein run 999
    > nine hundred and ninety nine

## Caveats

This was hobbled together pretty quickly. It uses an alpha release of clojure 1.9 which bundles `clojure.spec` as I wanted to try it out although I didn't get very far.

## Todo

* Add some generative tests!
* Clean up logic to rely less on whitespace trimming
* Have a rethink about the word library - I'm not sure I like it

## License

Copyright © 2016 neilwashere

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
