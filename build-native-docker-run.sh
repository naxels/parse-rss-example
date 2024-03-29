docker run -it --rm --name graalvm-inst1 \
           -v "$PWD":/target -w /target \
           findepi/graalvm:java21-native \
           native-image -jar target/parse-rss-example-*.jar --no-fallback --enable-https --features=clj_easy.graal_build_time.InitClojureClasses
