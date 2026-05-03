#!/bin/bash

# ============================================
# Script: check-coverage.sh
# Propósito: Ejecutar tests y validar coverage
# ============================================

set -e  # Salir si hay error

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

echo "🧪 Initializing code coverage check..."
echo "=================================================="

# Step 1: Ejecutar tests
echo "▶️  Step 1: Executing Unit Tests..."
if ./gradlew test; then
    echo "✅ Tests successfully executed"
else
    echo "❌ Tests failed"
    exit 1
fi

# Step 2: Generar reporte JaCoCo
echo ""
echo "▶️  Step 2: Generating JaCoCo Report..."
if ./gradlew jacocoTestReport; then
    echo "✅ Report generated successfully"
else
    echo "❌ Error generating report"
    exit 1
fi

# Step 3: Verificar cobertura
echo ""
echo "▶️  Step 3: Verifying coverage >= 80%..."
if ./gradlew verifyCodeCoverage; then
    echo "✅ Coverage validated"
    echo ""
    echo "=================================================="
    echo "📊 Summary: ¡Coverage meets requirements!"
    echo "=================================================="
    exit 0
else
    echo "❌ Not enough coverage. Please review the report for details."
    echo ""
    echo "💡 To view details: open app/build/reports/jacoco/test/html/index.html"
    exit 1
fi