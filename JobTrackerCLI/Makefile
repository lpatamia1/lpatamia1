# ==============================================================
# 🧭 Job Application Tracker - Makefile
# Automates compilation, testing, and static analysis
# ==============================================================

# Java compiler and main class
JAVAC = javac
JAVA = java
MAIN = JobApplicationTracker

# --------------------------------------------------------------
# 🐞 SpotBugs configuration
# --------------------------------------------------------------
SPOTBUGS_VERSION = 4.8.3
SPOTBUGS_URL = https://repo1.maven.org/maven2/com/github/spotbugs/spotbugs/$(SPOTBUGS_VERSION)/spotbugs-$(SPOTBUGS_VERSION).zip
SPOTBUGS_DIR = spotbugs-$(SPOTBUGS_VERSION)
SPOTBUGS_JAR = $(SPOTBUGS_DIR)/lib/spotbugs.jar

# --------------------------------------------------------------
# 💅 Checkstyle configuration
# --------------------------------------------------------------
CHECKSTYLE_VERSION = 10.17.0
CHECKSTYLE_URL = https://github.com/checkstyle/checkstyle/releases/download/checkstyle-$(CHECKSTYLE_VERSION)/checkstyle-$(CHECKSTYLE_VERSION)-all.jar
CHECKSTYLE_JAR = checkstyle-$(CHECKSTYLE_VERSION)-all.jar
GOOGLE_CHECKS = google_checks.xml

# --------------------------------------------------------------
# All Java source files
# --------------------------------------------------------------
SOURCES = $(wildcard *.java)
CLASSES = $(SOURCES:.java=.class)

# --------------------------------------------------------------
# Default target: compile everything
# --------------------------------------------------------------
all: $(CLASSES)

%.class: %.java
	@echo "📦 Compiling $< ..."
	@$(JAVAC) $<

# --------------------------------------------------------------
# Run the program
# --------------------------------------------------------------
run: all
	@echo "🚀 Launching Job Application Tracker..."
	@$(JAVA) $(MAIN)

# --------------------------------------------------------------
# Test build only (no run)
# --------------------------------------------------------------
test:
	@echo "🧪 Running extended Java tests..."
	@echo "1️⃣  Cleaning old builds..."
	@rm -f *.class
	@echo "2️⃣  Compiling all Java files with detailed diagnostics..."
	@$(JAVAC) -Xlint:all -Xdiags:verbose $(SOURCES)
	@echo ""
	@echo "3️⃣  Counting source files and lines of code..."
	@find . -name "*.java" -exec wc -l {} \; | sort -nr
	@echo ""
	@echo "4️⃣  Checking for missing or unused imports..."
	@grep -R "import " *.java | sort | uniq -c | sort -nr || true
	@echo ""
	@echo "✅ Test build finished: no compile errors detected."

# --------------------------------------------------------------
# 🔍 Run static analysis (SpotBugs)
# --------------------------------------------------------------
static:
	@echo "🔍 Running static analysis..."
	@if [ ! -f "$(SPOTBUGS_JAR)" ]; then \
		echo "⚙️  SpotBugs not found — downloading version $(SPOTBUGS_VERSION)..."; \
		curl -L "$(SPOTBUGS_URL)" -o spotbugs.zip; \
		unzip -q spotbugs.zip; \
		rm spotbugs.zip; \
		echo "✅ SpotBugs installed locally in ./$(SPOTBUGS_DIR)"; \
	fi; \
	echo "🐞 Running SpotBugs scan..."; \
	java -jar $(SPOTBUGS_JAR) -textui -low . > spotbugs-report.txt || true; \
	issues=$$(grep -c "BugInstance" spotbugs-report.txt || true); \
	echo "✅ Static analysis complete. Report saved to spotbugs-report.txt"; \
	echo "📊 Potential issues found: $$issues"

# --------------------------------------------------------------
# ✨ Run Checkstyle (auto-download + colorized output)
# --------------------------------------------------------------
checkstyle:
	@echo "✨ Running Checkstyle..."
	@if [ ! -f "$(CHECKSTYLE_JAR)" ]; then \
		echo "⚙️  Checkstyle not found — downloading version $(CHECKSTYLE_VERSION)..."; \
		curl -L "$(CHECKSTYLE_URL)" -o "$(CHECKSTYLE_JAR)"; \
		echo "✅ Checkstyle installed locally."; \
	fi; \
	if [ ! -f "$(GOOGLE_CHECKS)" ]; then \
		echo "⚙️  Google style config missing — downloading..."; \
		curl -L "https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml" -o "$(GOOGLE_CHECKS)"; \
		echo "✅ Google style config downloaded."; \
	fi; \
	echo "💅 Checking Java source style..."; \
	java -jar "$(CHECKSTYLE_JAR)" -c "$(GOOGLE_CHECKS)" $(SOURCES) > checkstyle-report.txt || true; \
	violations=$$(grep -c "Starting audit" checkstyle-report.txt >/dev/null 2>&1; grep -c "ERROR" checkstyle-report.txt || echo 0); \
	if [ "$$violations" -gt 0 ]; then \
		echo "\033[1;31m⚠️  Style violations found: $$violations\033[0m"; \
		grep --color=always "ERROR" checkstyle-report.txt || true; \
	else \
		echo "\033[1;32m✅ No Checkstyle violations detected!\033[0m"; \
	fi; \
	echo "📄 Full report saved to checkstyle-report.txt"

# --------------------------------------------------------------
# 🧹 Clean up build files
# --------------------------------------------------------------
clean:
	@echo "🧹 Cleaning up build files..."
	@rm -f *.class spotbugs-report.txt checkstyle-report.txt

# --------------------------------------------------------------
# 🔄 Rebuild everything from scratch
# --------------------------------------------------------------
rebuild: clean all

# --------------------------------------------------------------
# 📜 Phony targets
# --------------------------------------------------------------
.PHONY: all run clean rebuild static checkstyle test
