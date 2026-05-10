#!/bin/bash

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

REPORT_PATH="app/build/reports/jacoco/test/html"

if [[ ! -d "$REPORT_PATH" ]]; then
    echo "❌ Report not found. Run: make check-coverage"
    exit 1
fi

echo "📊 Opening coverage report..."
echo ""

cd "$REPORT_PATH"

# Start server in background
python3 -m http.server 8000 > /dev/null 2>&1 &
SERVER_PID=$!

# Wait a moment for server to start
sleep 1

# Open browser
open "http://localhost:8000/index.html"

echo "✅ Report opened in browser"
echo "💡 Server will close in 2 minutes of inactivity..."

# Kill server after 2 minutes
sleep 120
kill $SERVER_PID 2>/dev/null || true

echo "✅ Server closed"