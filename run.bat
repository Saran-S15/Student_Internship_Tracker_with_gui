@echo off
javac -cp ".;lib/mysql-connector-j-9.2.0.jar" *.java
java -cp ".;lib/mysql-connector-j-9.2.0.jar" Main
pause