@echo off
REM Compile and run Escape the Island. Works on Windows with a JDK 20+ installed.
REM No IDE required.
cd /d "%~dp0"

set "OUT=out\production\project-3-byow-Sunrise"
dir /s /b proj3\src\*.java > "%TEMP%\byow_srcs.txt"
javac -cp "lib\*" -d "%OUT%" @"%TEMP%\byow_srcs.txt"
if errorlevel 1 exit /b 1
java -cp "%OUT%;lib\*" core.Main
