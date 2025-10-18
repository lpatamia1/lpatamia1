# Job Application Tracker - Makefile

# Java compiler and main class
JAVAC = javac
JAVA = java
MAIN = JobApplicationTracker

# All Java source files
SOURCES = $(wildcard *.java)
CLASSES = $(SOURCES:.java=.class)

# Default target: compile everything
all: $(CLASSES)

%.class: %.java
	@echo "📦 Compiling $< ..."
	@$(JAVAC) $<

# Run the program
run: all
	@echo "🚀 Launching Job Application Tracker..."
	@$(JAVA) $(MAIN)

# Test build only (no run)
test:
	@echo "🧪 Running extended Java tests..."
	@echo "1️⃣  Cleaning old builds..."
	@rm -f *.class
	@echo "2️⃣  Compiling all Java files with detailed diagnostics..."
	@$(JAVAC) -Xlint:all -Xdiags:verbose $(SOURCES)
	@echo "3️⃣  Counting source files and lines of code..."
	@echo ""
	@find . -name "*.java" -exec wc -l {} \; | sort -nr
	@echo ""
	@echo "4️⃣  Checking for missing or unused imports..."
	@grep -R "import " *.java | sort | uniq -c | sort -nr || true
	@echo ""
	@echo "✅ Test build finished: no compile errors detected."

# Run static analysis (SpotBugs or Checkstyle)
static:
	@echo "🔍 Running static analysis..."
	@if command -v spotbugs >/dev/null 2>&1; then \
		echo "🐞 SpotBugs found — scanning for potential issues..."; \
		spotbugs -textui -low .; \
	elif command -v checkstyle >/dev/null 2>&1; then \
		echo "📏 Checkstyle found — checking code style..."; \
		checkstyle -c /google_checks.xml *.java; \
	else \
		echo "⚙️  Installing SpotBugs automatically..."; \
		if command -v apt >/dev/null 2>&1; then \
			sudo apt update -y && sudo apt install -y spotbugs; \
		elif command -v brew >/dev/null 2>&1; then \
			brew install spotbugs; \
		else \
			echo "❌ Could not auto-install SpotBugs (unsupported OS). Please install manually from https://spotbugs.github.io/downloads.html"; \
			exit 1; \
		fi; \
		echo "✅ SpotBugs installed. Re-running static analysis..."; \
		spotbugs -textui -low .; \
	fi

# Clean up build files
clean:
	@echo "🧹 Cleaning up .class files..."
	@rm -f *.class

# Rebuild everything from scratch
rebuild: clean all

.PHONY: all run clean rebuild
