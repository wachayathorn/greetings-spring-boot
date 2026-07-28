#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"

if [[ -z "${JAVA_HOME:-}" ]]; then
  if [[ -d "$ROOT/.jdks/jdk-21.0.7+6/Contents/Home" ]]; then
    export JAVA_HOME="$ROOT/.jdks/jdk-21.0.7+6/Contents/Home"
  elif [[ -d "/Library/Java/JavaVirtualMachines/temurin-26.jdk/Contents/Home" ]]; then
    export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-26.jdk/Contents/Home"
  fi
fi

if [[ -z "${JAVA_HOME:-}" ]]; then
  echo "JAVA_HOME is not set. Install JDK 21 or place Temurin under .jdks/" >&2
  exit 1
fi

export PATH="$JAVA_HOME/bin:$ROOT/.tools/apache-maven-3.9.9/bin:$PATH"

if ! command -v mvn >/dev/null 2>&1; then
  echo "mvn not found. Install Maven (brew install maven) or keep .tools/apache-maven-3.9.9/" >&2
  exit 1
fi

cd "$ROOT"

if [[ $# -eq 0 ]]; then
  echo "JAVA_HOME=$JAVA_HOME"
  java -version
  echo "---"
  exec mvn spring-boot:run
fi

exec mvn "$@"
