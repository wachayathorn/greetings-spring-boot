#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"

# Prefer project JDK 21 (LTS, Spring Boot friendly), then system JAVA_HOME
if [[ -z "${JAVA_HOME:-}" ]]; then
  if [[ -d "$ROOT/.jdks/jdk-21.0.7+6/Contents/Home" ]]; then
    export JAVA_HOME="$ROOT/.jdks/jdk-21.0.7+6/Contents/Home"
  elif [[ -d "/Library/Java/JavaVirtualMachines/temurin-26.jdk/Contents/Home" ]]; then
    export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-26.jdk/Contents/Home"
  fi
fi

export PATH="$JAVA_HOME/bin:$ROOT/.tools/apache-maven-3.9.9/bin:$PATH"

echo "JAVA_HOME=$JAVA_HOME"
java -version
echo "---"
cd "$ROOT"
exec mvn spring-boot:run "$@"
