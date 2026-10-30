@echo off
REM Compila e gera o JAR usando o caminho completo da JDK se necessario
if exist simulador-paginas.jar del /f /q simulador-paginas.jar
if exist out rmdir /s /q out
mkdir out

javac -encoding UTF-8 -d out src/simulador/*.java src/simulador/algoritmos/*.java src/simulador/gui/*.java || goto erro

"%JAVA_HOME%\bin\jar" --create --file simulador-paginas.jar --main-class simulador.Main -C out . || jar --create --file simulador-paginas.jar --main-class simulador.Main -C out . || goto erro

echo OK: simulador-paginas.jar gerado com sucesso!
goto fim

:erro
echo Falha na compilacao.
:fim