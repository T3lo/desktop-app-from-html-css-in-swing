echo off
cls
powershell -File init.ps1
dir /s/b src\java\*.java > internal\JAVA_FILES.txt
javac -d build @internal\JAVA_FILES.txt
echo Output:
echo on