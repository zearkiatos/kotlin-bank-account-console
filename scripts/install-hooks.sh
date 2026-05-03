#!/bin/bash

# ============================================
# Script: install-hooks.sh
# Propósito: Instalar pre-commit hook
# ============================================

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# Crear directorio de hooks si no existe
mkdir -p .git/hooks

# Crear archivo pre-commit hook
cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash

# ============================================
# Pre-commit Hook: Validate Code Coverage
# Block commits if coverage < 50%
# ============================================

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$PROJECT_ROOT"

echo ""
echo "🔍 Pre-commit hook: Verifying code coverage..."
echo "════════════════════════════════════════════════════"

# Execute validation of coverage
if ! bash scripts/check-coverage.sh; then
    echo ""
    echo "❌ Commit Blocked: Insufficient Coverage"
    echo ""
    echo "💡 Options:"
    echo "   1. Increase coverage by writing more tests"
    echo "   2. To skip: git commit --no-verify"
    echo "   3. View report: open app/build/reports/jacoco/test/html/index.html"
    echo ""
    exit 1
fi

echo ""
echo "✅ Pre-commit hook: Validation successful"
echo ""
EOF

# Hacer el hook ejecutable
chmod +x .git/hooks/pre-commit

echo "✅ Pre-commit hook installed successfully"
echo ""
echo "📝 Hook installed in: .git/hooks/pre-commit"
echo "🔄 Will be executed automatically before each commit"
echo ""
echo "💡 To skip the hook: git commit --no-verify"