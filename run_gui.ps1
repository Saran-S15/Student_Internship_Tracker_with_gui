Set-Location $PSScriptRoot
Write-Host "Compiling Student Internship Tracker GUI..." -ForegroundColor Cyan
javac -cp ".;lib/*" *.java gui/*.java gui/theme/*.java gui/components/*.java gui/dialogs/*.java gui/panels/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Starting Student Internship Tracker GUI Application..." -ForegroundColor Green
    java -cp ".;gui;gui/theme;gui/components;gui/dialogs;gui/panels;lib/*" GuiMain
} else {
    Write-Host "Compilation failed with errors." -ForegroundColor Red
}
