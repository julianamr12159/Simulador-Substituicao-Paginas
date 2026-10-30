#!/bin/sh
# Compila o projeto e gera simulador-paginas.jar (requer JDK 17 ou superior)
set -e
rm -rf out && mkdir out
javac -encoding UTF-8 -d out $(find src -name "*.java")
jar --create --file simulador-paginas.jar --main-class simulador.Main -C out .
echo "OK: simulador-paginas.jar gerado"
