@echo off
cd src
javac cabinetclasses/*.java gui/*.java MainGUI.java
if %errorlevel% equ 0 (
    echo Compilation réussie. Lancement de l'application...
    java MainGUI
) else (
    echo Erreur de compilation.
    pause
) 