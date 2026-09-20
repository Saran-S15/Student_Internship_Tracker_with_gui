@echo off
title Student Internship Tracker (FlatLaf Desktop GUI)
cd /d "%~dp0"
echo Compiling Student Internship Tracker GUI...
javac -cp ".;lib/*" *.java gui/*.java gui/theme/*.java gui/components/*.java gui/dialogs/*.java gui/panels/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Starting Student Internship Tracker GUI...
java -cp ".;gui;gui/theme;gui/components;gui/dialogs;gui/panels;lib/*" GuiMain
pause
