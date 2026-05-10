@echo off
REM ============================================
REM Script: check-coverage.bat
REM Propósito: Ejecutar tests y validar coverage
REM ============================================

setlocal enabledelayedexpansion

cd /d "%~dp0\.."

echo 🧪 Initializing code coverage check...
echo ==================================================

REM Step 1: Ejecutar tests
echo.
echo ▶️  Step 1: Executing Unit Tests...
call gradlew.bat test
if errorlevel 1 (
    echo ❌ Tests failed
    exit /b 1
)
echo ✅ Tests successfully executed

REM Step 2: Generar reporte JaCoCo
echo.
echo ▶️  Step 2: Generating JaCoCo Report...
call gradlew.bat jacocoTestReport
if errorlevel 1 (
    echo ❌ Error generating report
    exit /b 1
)
echo ✅ Report generated successfully

REM Step 3: Verificar cobertura
echo.
echo ▶️  Step 3: Verifying coverage >= 80%%...
call gradlew.bat verifyCodeCoverage
if errorlevel 1 (
    echo ❌ Not enough coverage. Please review the report for details.
    echo.
    echo 💡 To view details: start app\build\reports\jacoco\test\html\index.html
    exit /b 1
)

echo ✅ Coverage validated
echo.
echo ==================================================
echo 📊 Summary: ¡Coverage meets requirements!
echo ==================================================
exit /b 0