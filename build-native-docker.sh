docker container run                                                     \
    --volume $(pwd)/target-linux:/app/target                             \
    --rm -it --name build_native_parse_rss_example clojure-native-image