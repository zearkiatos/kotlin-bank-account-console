#!/bin/bash

# ============================================
# Script: check-coverage.sh
# Purpose: Execute tests, generate JaCoCo report and verify code coverage >= 80%
# ============================================

set -e  # Exit on error

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# ============================================
# Function to extract coverage percentage from JaCoCo XML report
# ============================================
extract_coverage() {
    local xml_file="app/build/reports/jacoco/test/jacocoTestReport.xml"
    
    if [[ ! -f "$xml_file" ]]; then
        echo "❌ XML file not found: $xml_file"
        echo "0"
        return 1
    fi
    
    # Extract the last LINE counter (the total coverage)
    local last_line_counter=$(grep 'type="LINE"' "$xml_file" | tail -1)
    
    if [[ -z "$last_line_counter" ]]; then
        echo "❌ No LINE counter found in XML"
        echo "0"
        return 1
    fi
    
    # Extract missed and covered values using sed
    local missed=$(echo "$last_line_counter" | sed -E 's/.*missed="([0-9]+)".*/\1/')
    local covered=$(echo "$last_line_counter" | sed -E 's/.*covered="([0-9]+)".*/\1/')
    
    if [[ -z "$missed" || -z "$covered" ]]; then
        echo "❌ Could not parse missed/covered values"
        echo "0"
        return 1
    fi
    
    local total=$((missed + covered))
    
    if [[ $total -eq 0 ]]; then
        echo "❌ Total lines is 0"
        echo "0"
        return 1
    fi
    
    local percentage=$(( (covered * 100) / total ))
    echo "$percentage"
}

# ============================================
# Function to colorize coverage percentage output
# ============================================
color_percentage() {
    local percentage=$1
    local red='\033[0;31m'
    local green='\033[0;32m'
    local yellow='\033[1;33m'
    local nc='\033[0m'  # No Color
    
    if [[ $percentage -lt 60 ]]; then
        echo -e "${red}${percentage}%${nc}"
    elif [[ $percentage -lt 80 ]]; then
        echo -e "${yellow}${percentage}%${nc}"
    else
        echo -e "${green}${percentage}%${nc}"
    fi
}

echo "🧪 Initializing code coverage check..."
echo "=================================================="

# Step 1: Execute unit tests
echo "▶️  Step 1: Executing Unit Tests..."
if ./gradlew test; then
    echo "✅ Tests successfully executed"
else
    echo "❌ Tests failed"
    exit 1
fi

# Step 2: Generate JaCoCo report
echo ""
echo "▶️  Step 2: Generating JaCoCo Report..."
if ./gradlew jacocoTestReport; then
    echo "✅ Report generated successfully"
else
    echo "❌ Error generating report"
    exit 1
fi

# Step 3: Extract current coverage percentage
echo ""
echo "▶️  Step 3: Extracting coverage from XML..."
COVERAGE=$(extract_coverage)
COVERAGE_COLORED=$(color_percentage "$COVERAGE")

# Step 4: Verify code coverage meets minimum threshold
echo ""
echo "▶️  Step 4: Verifying coverage >= 80%..."

# Capture the exit code from verifyCodeCoverage WITHOUT exiting the script
set +e
./gradlew verifyCodeCoverage
VERIFY_EXIT_CODE=$?
set -e

echo ""

if [[ $VERIFY_EXIT_CODE -eq 0 ]]; then
    echo "╔════════════════════════════════════════╗"
    echo "║       ✨ COVERAGE VALIDATION OK ✨    ║"
    echo "╠════════════════════════════════════════╣"
    echo "║  Current Coverage: $COVERAGE_COLORED               ║"
    echo "║  Minimum Required: 80%                 ║"
    echo "║  Status: ✅ APPROVED                  ║"
    echo "╚════════════════════════════════════════╝"
    echo ""
    exit 0
else
    echo "╔════════════════════════════════════════╗"
    echo "║      ❌ COVERAGE VALIDATION FAILED ❌  ║"
    echo "╠════════════════════════════════════════╣"
    echo "║  Current Coverage: $COVERAGE_COLORED               ║"
    echo "║  Minimum Required: 80%                 ║"
    echo "║  Status: ⚠️  NOT APPROVED              ║"
    echo "╚════════════════════════════════════════╝"
    echo ""
    echo "💡 To view details: open app/build/reports/jacoco/test/html/index.html"
    exit 1
fi