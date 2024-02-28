# GraalVM 21.0.2
$GRAALVM_HOME/bin/native-image -jar target/parse-rss-example-*.jar --no-fallback --enable-http --enable-https --features=clj_easy.graal_build_time.InitClojureClasses
