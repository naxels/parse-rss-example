# Parse RSS Example using Clojure

Simple example to show how to parse an RSS feed using Clojure and deps.edn

Not very organized, but shows the steps involved when you don't know the data structure
and how to explore that data step by step and transform it.

Then ultimately it's abstracted into functions and composed into outputting the RSS's item titles on the commandline

# Run the code
> ./run.sh "(rss url)"

# Babashka
> ./src/core-bb.clj (rss url)

# Uberjar

To generate an Uberjar, run:
> ./build-uberjar.sh

Then run
> java -jar target/parse_rss-example-(version).jar (rss url)

# GraalVM Native image

**Tested with GraalVM CE 21.0.2**

**After creating the uberjar**
> ./build-native.sh

If you want to generate a native image for the linux platform (and don't use linux as host), use Docker:
> ./build-native-docker.sh

or build Dockerfile / image: -untested with newer versions-
> ./build-docker-image.sh
