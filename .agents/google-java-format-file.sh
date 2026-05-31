#!/usr/bin/env sh

set -eu

if [ "$#" -eq 0 ]; then
  echo "Usage: $(basename "$0") <JavaFile> [more Java files...]"
  echo "Example: $(basename "$0") src/main/java/com/vulinh/utils/CommonUtils.java"
  exit 1
fi

if [ -z "${JAVA_HOME:-}" ]; then
  echo "JAVA_HOME is not set. Google Java Format requires JDK 25 or newer for this project."
  exit 1
fi

JAVA_EXE="$JAVA_HOME/bin/java"

if [ ! -x "$JAVA_EXE" ]; then
  echo "JAVA_HOME does not point to a valid JDK: $JAVA_HOME"
  echo "Missing Java executable: $JAVA_EXE"
  exit 1
fi

JAVA_VERSION_RAW=$("$JAVA_EXE" -version 2>&1 | awk '/version/ { gsub(/"/, "", $3); print $3; exit }')

if [ -z "$JAVA_VERSION_RAW" ]; then
  echo "Could not determine Java version from $JAVA_EXE"
  exit 1
fi

JAVA_MAJOR=$(printf '%s\n' "$JAVA_VERSION_RAW" | awk -F. '{ if ($1 == "1") print $2; else print $1 }')

case "$JAVA_MAJOR" in
  ''|*[!0-9]*)
    echo "Could not determine Java major version from $JAVA_VERSION_RAW"
    exit 1
    ;;
esac

if [ "$JAVA_MAJOR" -lt 25 ]; then
  echo "JAVA_HOME must point to JDK 25 or newer for Google Java Format."
  echo "Current JAVA_HOME: $JAVA_HOME"
  echo "Current Java version: $JAVA_VERSION_RAW"
  exit 1
fi

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
VERSION_FILE="$SCRIPT_DIR/google-java-format.version"

if [ ! -f "$VERSION_FILE" ]; then
  echo "Missing formatter version file: $VERSION_FILE"
  exit 1
fi

property_value() {
  key=$1

  awk -F= -v key="$key" '$1 == key { value = substr($0, index($0, "=") + 1); gsub(/[[:space:]]/, "", value); print value; exit }' "$VERSION_FILE"
}

GJF_REPOSITORY_URL=$(property_value repositoryUrl)
GJF_GROUP_PATH=$(property_value groupPath)
GJF_ARTIFACT_ID=$(property_value artifactId)
GJF_CLASSIFIER=$(property_value classifier)
GJF_VERSION=$(property_value version)

if [ -z "$GJF_REPOSITORY_URL" ]; then
  echo "Could not resolve repositoryUrl property from $VERSION_FILE"
  exit 1
fi

if [ -z "$GJF_GROUP_PATH" ]; then
  echo "Could not resolve groupPath property from $VERSION_FILE"
  exit 1
fi

if [ -z "$GJF_ARTIFACT_ID" ]; then
  echo "Could not resolve artifactId property from $VERSION_FILE"
  exit 1
fi

if [ -z "$GJF_CLASSIFIER" ]; then
  echo "Could not resolve classifier property from $VERSION_FILE"
  exit 1
fi

if [ -z "$GJF_VERSION" ]; then
  echo "Could not resolve version property from $VERSION_FILE"
  exit 1
fi

GJF_ARTIFACT="$GJF_ARTIFACT_ID-$GJF_VERSION-$GJF_CLASSIFIER.jar"
GJF_CACHE_DIR="$SCRIPT_DIR"
GJF_JAR_FILE="$GJF_CACHE_DIR/$GJF_ARTIFACT"
GJF_SHA256_FILE="$GJF_JAR_FILE.sha256"
GJF_BASE_URL="$GJF_REPOSITORY_URL/$GJF_GROUP_PATH/$GJF_ARTIFACT_ID/$GJF_VERSION"
GJF_JAR_URL="$GJF_BASE_URL/$GJF_ARTIFACT"
GJF_SHA256_URL="$GJF_JAR_URL.sha256"

download_file() {
  url=$1
  output=$2

  echo "Downloading $url"
  tmp_output="$output.download"

  if command -v curl >/dev/null 2>&1; then
    curl -fL --retry 3 -o "$tmp_output" "$url"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$tmp_output" "$url"
  else
    echo "Missing downloader: install curl or wget."
    exit 1
  fi

  mv "$tmp_output" "$output"
}

if [ ! -f "$GJF_JAR_FILE" ]; then
  download_file "$GJF_JAR_URL" "$GJF_JAR_FILE"
fi

if [ ! -f "$GJF_SHA256_FILE" ]; then
  download_file "$GJF_SHA256_URL" "$GJF_SHA256_FILE"
fi

EXPECTED_SHA256=$(awk '{print tolower($1); exit}' "$GJF_SHA256_FILE")

if [ -z "$EXPECTED_SHA256" ]; then
  echo "Formatter checksum file is empty: $GJF_SHA256_FILE"
  exit 1
fi

if command -v sha256sum >/dev/null 2>&1; then
  ACTUAL_SHA256=$(sha256sum "$GJF_JAR_FILE" | awk '{print tolower($1)}')
elif command -v shasum >/dev/null 2>&1; then
  ACTUAL_SHA256=$(shasum -a 256 "$GJF_JAR_FILE" | awk '{print tolower($1)}')
else
  echo "Missing checksum tool: install sha256sum or shasum."
  exit 1
fi

if [ "$EXPECTED_SHA256" != "$ACTUAL_SHA256" ]; then
  echo "Formatter jar checksum mismatch: $GJF_JAR_FILE"
  echo "Expected: $EXPECTED_SHA256"
  echo "Actual:   $ACTUAL_SHA256"
  exit 1
fi

"$JAVA_EXE" \
  --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED \
  --add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED \
  --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
  --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
  --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
  --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED \
  -jar "$GJF_JAR_FILE" \
  --replace \
  --skip-reflowing-long-strings \
  "$@"
