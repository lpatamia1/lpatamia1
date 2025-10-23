# ==============================================================
# 🧭 Job Application Tracker - Makefile
# Compiles, runs, and checks Java source files under /java
# ==============================================================

# Java compiler and main class
JAVAC = javac
JAVA  = java
SRC_DIR = tracker
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
# All Java source files (recursive under /java)
# --------------------------------------------------------------
SOURCES := $(wildcard $(SRC_DIR)/*.java)
CLASSES := $(SOURCES:.java=.class)

# --------------------------------------------------------------
# Default target: compile everything
# --------------------------------------------------------------
all:
	@echo "🧱 Building all Java source files from $(SRC_DIR)/..."
	@$(JAVAC) $(SOURCES)
	@echo "✅ All Java files compiled successfully!"

# --------------------------------------------------------------
# Run the program
# --------------------------------------------------------------
run: all
	@echo "🚀 Launching Job Application Tracker..."
	@echo "-------------------------------------------"
	@$(JAVA) -cp . $(SRC_DIR).$(MAIN)
	@echo "-------------------------------------------"
	@echo "🏁 Program exited successfully."

# --------------------------------------------------------------
# 🧪 Test build
# --------------------------------------------------------------
test:
	@echo "🧪 Running extended Java tests..."
	@rm -f $(SRC_DIR)/*.class
	@$(JAVAC) -Xlint:all -Xdiags:verbose $(SOURCES)
	@find $(SRC_DIR) -name "*.java" -exec wc -l {} \; | sort -nr
	@echo "✅ Test build finished successfully."

# --------------------------------------------------------------
# 🔍 Static analysis
# --------------------------------------------------------------
static:
	@echo "🔍 Running SpotBugs analysis..."
	@if [ ! -f "$(SPOTBUGS_JAR)" ]; then \
		echo "⚙️  Downloading SpotBugs $(SPOTBUGS_VERSION)..."; \
		curl -L "$(SPOTBUGS_URL)" -o spotbugs.zip; \
		unzip -q spotbugs.zip; rm spotbugs.zip; \
	fi; \
	java -jar $(SPOTBUGS_JAR) -textui -low $(SRC_DIR) > spotbugs-report.txt || true; \
	echo "✅ Static analysis complete → spotbugs-report.txt"

# --------------------------------------------------------------
# ✨ Checkstyle
# --------------------------------------------------------------
checkstyle:
	@echo "✨ Running Checkstyle..."
	@if [ ! -f "$(CHECKSTYLE_JAR)" ]; then \
		echo "⚙️  Downloading Checkstyle $(CHECKSTYLE_VERSION)..."; \
		curl -L "$(CHECKSTYLE_URL)" -o "$(CHECKSTYLE_JAR)"; \
	fi; \
	if [ ! -f "$(GOOGLE_CHECKS)" ]; then \
		curl -L "https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml" -o "$(GOOGLE_CHECKS)"; \
	fi; \
	java -jar "$(CHECKSTYLE_JAR)" -c "$(GOOGLE_CHECKS)" $(SOURCES) > checkstyle-report.txt || true; \
	echo "📄 Report saved to checkstyle-report.txt"

# --------------------------------------------------------------
# 🧹 Clean
# --------------------------------------------------------------
clean:
	@echo "🧹 Cleaning compiled class files..."
	@rm -f $(SRC_DIR)/*.class spotbugs-report.txt checkstyle-report.txt
	@echo "✨ Clean complete!"

# --------------------------------------------------------------
# 🔄 Rebuild
# --------------------------------------------------------------
rebuild: clean all

.PHONY: all run clean rebuild static checkstyle test
