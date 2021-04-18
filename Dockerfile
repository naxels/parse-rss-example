FROM findepi/graalvm:java11-native

# from https://github.com/Quantisan/docker-clojure/blob/master/target/openjdk-11-slim-buster/latest/Dockerfile
### INSTALL TOOLS-DEPS ###
# Release 2021-04-03
ENV CLOJURE_VERSION=1.10.3.822

WORKDIR /tmp

RUN \
apt-get update && \
apt-get install -y curl make rlwrap wget && \
rm -rf /var/lib/apt/lists/* && \
wget https://download.clojure.org/install/linux-install-$CLOJURE_VERSION.sh && \
sha256sum linux-install-$CLOJURE_VERSION.sh && \
echo "ebc820fe0e74de4bd77e6d5bd7db4a262ec1902efdf4d0553309485afcd75abf *linux-install-$CLOJURE_VERSION.sh" | sha256sum -c - && \
chmod +x linux-install-$CLOJURE_VERSION.sh && \
./linux-install-$CLOJURE_VERSION.sh && \
clojure -e "(clojure-version)" && \
apt-get purge -y --auto-remove curl wget

# warning: native-image compilation might require more memory than assigned to Docker
# so when error: Error: Image building with exit status 137
# increase Docker resource memory allowance

#setup /app folder
# RUN mkdir /app

# add current folder as volume
# VOLUME . /app
COPY . /app

WORKDIR /app

# set env
ENV GRAALVM_HOME=/graalvm

# do a Prepare for execution run so you don't have to wait for dependency download
# https://clojure.org/reference/deps_and_cli#_prepare_for_execution
RUN clj -P -M:native-image

# RUN ./create-native.sh
ENTRYPOINT ./create-native.sh