# Parse RSS Example using Clojure

Simple example to show how to parse an RSS feed using Clojure standard lib and deps.edn

Not very organized, but shows the steps involved when you don't know the data structure
and how to explore that data step by step and transform it.

Then ultimately it's abstracted into functions and composed into outputting the RSS's item titles on the commandline

# Run the code
> ./run.sh

# GraalVM Native image

If you have GraalVM installed and setup, you can generate a Native image using:
> ./build-native.sh

If you want to generate a native image for the linux platform (and don't use linux as host), use Docker:
> ./build-docker-image.sh
> ./build-native-docker.sh

This will generate the executable in the target-linux folder
# Uberjar

To generate an Uberjar, run:
> ./build-uberjar.sh