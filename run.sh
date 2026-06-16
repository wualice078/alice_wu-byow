#!/usr/bin/env bash
# Compile and run Escape the Island. Works on macOS/Linux with a JDK 20+ installed.
# No IDE required.
set -e
cd "$(dirname "$0")"

OUT="out/production/project-3-byow-Sunrise"
javac -cp "lib/*" -d "$OUT" $(find proj3/src -name "*.java")
java -cp "$OUT:lib/*" core.Main
